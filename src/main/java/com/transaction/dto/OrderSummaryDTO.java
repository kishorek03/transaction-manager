package com.transaction.dto;

import java.math.BigDecimal;

public record OrderSummaryDTO(
        Long id, BigDecimal totalAmount, String paymentMethod) {}
