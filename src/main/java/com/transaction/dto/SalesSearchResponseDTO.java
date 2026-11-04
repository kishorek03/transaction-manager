package com.transaction.dto;

import java.util.List;

public record SalesSearchResponseDTO(
        List<SaleDTO> sales,
        SalesUnitSummaryDTO summary,
        Integer currentPage,
        Integer totalPages,
        Long totalElements,
        Boolean hasMore
) {
    public SalesSearchResponseDTO(List<SaleDTO> sales, SalesUnitSummaryDTO summary) {
        this(sales, summary, 0, 1, sales != null ? (long) sales.size() : 0L, false);
    }
}

