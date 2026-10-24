package com.transaction.dto;

import java.math.BigDecimal;

public record UnitQuantityCountDTO(
        Long unitId,
        String unitName,
        String unitAbbreviation,
        BigDecimal totalQuantity,
        Long saleCount,
        BigDecimal totalAmount
) {}
