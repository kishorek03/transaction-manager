package com.transaction.dto;

import jakarta.validation.constraints.NotBlank;

public record PaymentMethodDTO(

    @NotBlank(message = "Payment method name cannot be blank")
    String name

) {}
