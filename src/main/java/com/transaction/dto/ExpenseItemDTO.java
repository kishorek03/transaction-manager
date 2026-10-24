package com.transaction.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ExpenseItemDTO(
        Long id,
        @NotBlank(message = "Name cannot be empty")
        String name,
        String tamilName,
        @NotNull(message = "Category ID cannot be null")
        Long categoryId,
        String categoryName,
        Long defaultUnitId,
        String defaultUnitName,
        BigDecimal defaultPrice,
        Boolean isActive
) {}
