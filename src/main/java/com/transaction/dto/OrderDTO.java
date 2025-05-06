package com.transaction.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

public record OrderDTO(

    Long id,

    @NotNull(message = "Payment method ID is required")
    Long paymentMethodId,

    @NotNull(message = "Total amount is required")
    @PositiveOrZero(message = "Total amount must be zero or positive")
    BigDecimal totalAmount,

    @NotEmpty(message = "At least one sale is required")
    List<@Valid SaleDTO> sales

) {}
