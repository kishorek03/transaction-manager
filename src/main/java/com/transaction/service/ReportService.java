package com.transaction.service;

import com.transaction.dto.CategoryExpenseSummaryDTO;
import com.transaction.dto.PnLPeriodDTO;
import com.transaction.dto.PnLReportDTO;
import com.transaction.dto.projection.CategoryAggregateProjection;
import com.transaction.dto.projection.PeriodAggregateProjection;
import com.transaction.dto.projection.TotalsAggregateProjection;
import com.transaction.repository.ExpenseRepository;
import com.transaction.repository.OrderRepository;
import com.transaction.util.DateRange;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReportService {

    private final OrderRepository orderRepository;
    private final ExpenseRepository expenseRepository;

    public PnLReportDTO getPnLReport(LocalDate from, LocalDate to, LocalDateTime start, LocalDateTime end, String groupBy) {
        // If neither from/to nor start/end provided, default to current year
        if (from == null && to == null && start == null && end == null) {
            from = LocalDate.now().withDayOfYear(1);
            to = LocalDate.now();
        }

        DateRange range = DateRange.resolve(from, to, start, end).requireBounds();
        LocalDate displayFrom = from != null ? from : range.start().toLocalDate();
        LocalDate displayTo = to != null ? to : range.end().toLocalDate().minusDays(1);

        String dateFormat;
        if (groupBy != null && groupBy.equalsIgnoreCase("DAY")) {
            dateFormat = "%Y-%m-%d";
        } else if (groupBy != null && groupBy.equalsIgnoreCase("MONTH")) {
            dateFormat = "%Y-%m";
        } else {
            // Auto-detect based on duration
            long days = ChronoUnit.DAYS.between(range.start().toLocalDate(), range.end().toLocalDate());
            dateFormat = (days <= 35) ? "%Y-%m-%d" : "%Y-%m";
        }

        TotalsAggregateProjection salesTotals = orderRepository.getSalesTotalsBetween(range.start(), range.end());
        TotalsAggregateProjection expenseTotals = expenseRepository.getExpenseTotalsBetween(range.start(), range.end());

        BigDecimal totalSales = (salesTotals != null && salesTotals.getTotal() != null) ? salesTotals.getTotal() : BigDecimal.ZERO;
        Long totalOrders = (salesTotals != null && salesTotals.getCount() != null) ? salesTotals.getCount() : 0L;
        BigDecimal cashSales = (salesTotals != null && salesTotals.getCash() != null) ? salesTotals.getCash() : BigDecimal.ZERO;
        BigDecimal upiSales = (salesTotals != null && salesTotals.getUpi() != null) ? salesTotals.getUpi() : BigDecimal.ZERO;

        BigDecimal totalExpenses = (expenseTotals != null && expenseTotals.getTotal() != null) ? expenseTotals.getTotal() : BigDecimal.ZERO;
        Long totalExpensesCount = (expenseTotals != null && expenseTotals.getCount() != null) ? expenseTotals.getCount() : 0L;
        BigDecimal cashExpenses = (expenseTotals != null && expenseTotals.getCash() != null) ? expenseTotals.getCash() : BigDecimal.ZERO;
        BigDecimal upiExpenses = (expenseTotals != null && expenseTotals.getUpi() != null) ? expenseTotals.getUpi() : BigDecimal.ZERO;

        BigDecimal netProfitOrLoss = totalSales.subtract(totalExpenses);
        Double profitMargin = calculateMargin(netProfitOrLoss, totalSales);

        // Periods breakdown
        List<PeriodAggregateProjection> salesByPeriod = orderRepository.getSalesByPeriod(range.start(), range.end(), dateFormat);
        List<PeriodAggregateProjection> expensesByPeriod = expenseRepository.getExpensesByPeriod(range.start(), range.end(), dateFormat);

        Map<String, PeriodAggregateProjection> salesMap = new HashMap<>();
        for (PeriodAggregateProjection p : salesByPeriod) {
            salesMap.put(p.getPeriod(), p);
        }

        Map<String, PeriodAggregateProjection> expenseMap = new HashMap<>();
        for (PeriodAggregateProjection p : expensesByPeriod) {
            expenseMap.put(p.getPeriod(), p);
        }

        Set<String> allPeriods = new TreeSet<>();
        allPeriods.addAll(salesMap.keySet());
        allPeriods.addAll(expenseMap.keySet());

        List<PnLPeriodDTO> periodDTOs = new ArrayList<>();
        for (String p : allPeriods) {
            PeriodAggregateProjection s = salesMap.get(p);
            PeriodAggregateProjection e = expenseMap.get(p);

            BigDecimal sAmt = (s != null && s.getTotal() != null) ? s.getTotal() : BigDecimal.ZERO;
            Long sCnt = (s != null && s.getCount() != null) ? s.getCount() : 0L;

            BigDecimal eAmt = (e != null && e.getTotal() != null) ? e.getTotal() : BigDecimal.ZERO;
            Long eCnt = (e != null && e.getCount() != null) ? e.getCount() : 0L;

            BigDecimal pnl = sAmt.subtract(eAmt);
            Double margin = calculateMargin(pnl, sAmt);

            periodDTOs.add(new PnLPeriodDTO(p, sAmt, eAmt, pnl, margin, sCnt, eCnt));
        }

        // Category Expense breakdown
        List<CategoryAggregateProjection> catAggregates = expenseRepository.getCategoryExpensesBetween(range.start(), range.end());
        List<CategoryExpenseSummaryDTO> categoryDTOs = new ArrayList<>();
        for (CategoryAggregateProjection c : catAggregates) {
            BigDecimal catTotal = c.getTotal() != null ? c.getTotal() : BigDecimal.ZERO;
            Double catPercentage = (totalExpenses.compareTo(BigDecimal.ZERO) > 0)
                    ? catTotal.divide(totalExpenses, 4, RoundingMode.HALF_UP).doubleValue() * 100
                    : 0.0;

            categoryDTOs.add(new CategoryExpenseSummaryDTO(c.getCategoryId(), c.getCategoryName(), catTotal, Math.round(catPercentage * 10.0) / 10.0));
        }

        return new PnLReportDTO(
                displayFrom,
                displayTo,
                totalSales,
                totalExpenses,
                netProfitOrLoss,
                profitMargin,
                totalOrders,
                totalExpensesCount,
                cashSales,
                upiSales,
                cashExpenses,
                upiExpenses,
                periodDTOs,
                categoryDTOs
        );
    }

    private Double calculateMargin(BigDecimal net, BigDecimal totalSales) {
        if (totalSales == null || totalSales.compareTo(BigDecimal.ZERO) <= 0) {
            return 0.0;
        }
        return Math.round(net.divide(totalSales, 4, RoundingMode.HALF_UP).doubleValue() * 1000.0) / 10.0;
    }
}
