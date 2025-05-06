package com.transaction.dto;

import com.transaction.entity.Category;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

// Product DTO
public record ProductDTO(
        @NotBlank(message = "Name cannot be empty")
        String name,

        @Positive(message = "Base Price must be positive")
        BigDecimal basePrice,

        @Positive(message = "Unit Price must be positive")
        BigDecimal unitPrice,

        @NotNull(message = "Unit ID cannot be null")
        Long unitId,

        @Positive(message = "Parcel Price must be positive")
        BigDecimal parcelPrice,

        @NotNull(message = "Category ID cannot be null")
        Long categoryId,
        // Optional lists, they can be null
        List<Long> flavourIds,
        List<Long> addOnIds
) {}