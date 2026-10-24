package com.transaction.dto;

import java.util.List;

public record SalesSearchResponseDTO(
        List<SaleDTO> sales,
        SalesUnitSummaryDTO summary
) {}
