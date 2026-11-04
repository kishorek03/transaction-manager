package com.transaction.dto;

import java.math.BigDecimal;

public record PnLPeriodDTO(
        String period,
        BigDecimal sales,
        BigDecimal expenses,
        BigDecimal netProfitOrLoss,
        Double profitMarginPercentage,
        Long orderCount,
        Long expenseCount,
        Double sugarcaneKg,
        Double sugarcaneGlasses,
        Double sugarcaneAvgGlassPerKg,
        Double milkLitres,
        Double buttermilkGlasses,
        Double buttermilkAvgGlassPerLitre
) {
    public PnLPeriodDTO(
            String period,
            BigDecimal sales,
            BigDecimal expenses,
            BigDecimal netProfitOrLoss,
            Double profitMarginPercentage,
            Long orderCount,
            Long expenseCount
    ) {
        this(period, sales, expenses, netProfitOrLoss, profitMarginPercentage, orderCount, expenseCount,
                0.0, 0.0, null, 0.0, 0.0, null);
    }
}
