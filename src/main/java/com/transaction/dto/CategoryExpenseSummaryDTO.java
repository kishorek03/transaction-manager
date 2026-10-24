package com.transaction.dto;

import java.math.BigDecimal;

public record CategoryExpenseSummaryDTO(
        Long categoryId,
        String categoryName,
        BigDecimal totalAmount,
        Double percentageOfTotal
) {}
