package com.transaction.dto;

import java.math.BigDecimal;

public record PnLPeriodDTO(
        String period,
        BigDecimal sales,
        BigDecimal expenses,
        BigDecimal netProfitOrLoss,
        Double profitMarginPercentage,
        Long orderCount,
        Long expenseCount
) {}
