package com.transaction.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record PnLReportDTO(
        LocalDate fromDate,
        LocalDate toDate,
        BigDecimal totalSales,
        BigDecimal totalExpenses,
        BigDecimal netProfitOrLoss,
        Double profitMarginPercentage,
        Long totalOrders,
        Long totalExpensesCount,
        BigDecimal cashSales,
        BigDecimal upiSales,
        BigDecimal cashExpenses,
        BigDecimal upiExpenses,
        List<PnLPeriodDTO> periods,
        List<CategoryExpenseSummaryDTO> expenseBreakdown
) {}
