package com.transaction.dto;

import java.math.BigDecimal;

public record OrderSummaryDTO(BigDecimal totalAmount, String paymentMethod) {}
