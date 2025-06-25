package com.transaction.cron;

import com.transaction.entity.DailyCashBalance;
import com.transaction.repository.CashMovementRepository;
import com.transaction.repository.DailyCashBalanceRepository;
import com.transaction.repository.ExpenseRepository;
import com.transaction.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CashBalanceCronService {

    private final SaleRepository saleRepository;
    private final ExpenseRepository expenseRepository;
    private final CashMovementRepository cashMovementRepository;
    private final DailyCashBalanceRepository dailyCashBalanceRepository;

    // Runs every day at 12:10 AM
    @Scheduled(cron = "0 10 0 * * *")
    @Transactional
    public void calculateAndSaveDailyCashBalance() {
        LocalDate today = LocalDate.now();

        if (dailyCashBalanceRepository.findByBusinessDate(today).isPresent()) {
            return; // Already created
        }

        LocalDate yesterday = today.minusDays(1);

        BigDecimal previousBalance = dailyCashBalanceRepository.findByBusinessDate(yesterday)
                .map(DailyCashBalance::getAmount)
                .orElse(BigDecimal.ZERO);

        BigDecimal totalCashSales = saleRepository.getTotalSalesByDateAndCash(today).orElse(BigDecimal.ZERO);
        BigDecimal totalCashExpenses = expenseRepository.getTotalExpensesByDateAndCash(today).orElse(BigDecimal.ZERO);
        BigDecimal netCashMovements = cashMovementRepository.getNetCashMovementByDate(today).orElse(BigDecimal.ZERO);

        BigDecimal closingBalance = previousBalance
                .add(totalCashSales)
                .subtract(totalCashExpenses)
                .add(netCashMovements);

        DailyCashBalance balance = DailyCashBalance.builder()
                .businessDate(today)
                .amount(closingBalance)
                .userId("SYSTEM")
                .build();

        dailyCashBalanceRepository.save(balance);
    }
}

