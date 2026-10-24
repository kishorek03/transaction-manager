package com.transaction.dto;

import java.math.BigDecimal;
import java.util.List;

public record SalesUnitSummaryDTO(
        Long totalSalesCount,
        BigDecimal totalAmount,
        Boolean allSameUnit,
        Long sameUnitId,
        String sameUnitName,
        String sameUnitAbbreviation,
        BigDecimal sameUnitTotalQuantity,
        List<UnitQuantityCountDTO> unitQuantities
) {}
