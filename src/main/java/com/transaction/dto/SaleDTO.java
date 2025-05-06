package com.transaction.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record SaleDTO(
        @NotNull(message = "Product ID cannot be null")
        @Positive(message = "Product ID must be positive") Long productId,

        @Positive(message = "Flavour ID must be positive") Long flavourId,

        @Positive(message = "Add-on ID must be positive") Long addOnId,

        @NotNull(message = "Quantity cannot be null")
        @Min(value = 1, message = "Quantity must be at least 1") BigDecimal quantity,

        @NotNull(message = "Amount cannot be null")
        @Positive(message = "Amount must be positive") BigDecimal amount,

        @PositiveOrZero(message = "Sale Price must be non-negative") BigDecimal salePrice,

        @NotNull(message = "Parcel flag cannot be null") Boolean parcel,

        Long orderId
) {}
