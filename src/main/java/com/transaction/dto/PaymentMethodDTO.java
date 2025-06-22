package com.transaction.dto;

import jakarta.validation.constraints.NotBlank;

public record PaymentMethodDTO(
        Long id,
    @NotBlank(message = "Payment method name cannot be blank")
    String name

) {}
