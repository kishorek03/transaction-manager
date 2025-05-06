package com.transaction.dto;


import com.transaction.entity.CategoryType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryDTO(
    Long id,

    @NotBlank(message = "Category name is required")
    String categoryName,

    @NotNull(message = "Category type is required")
    CategoryType categoryType,

    @NotNull(message = "Recurring flag must be specified")
    Boolean isRecurring,

    String description
) {}