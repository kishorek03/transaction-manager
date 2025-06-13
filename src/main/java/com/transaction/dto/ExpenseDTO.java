package com.transaction.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ExpenseDTO(
        Long id,
        String item,
        BigDecimal quantity,
        BigDecimal amount,
        Long unitId,
        String unitName,
        Long categoryId,
        String categoryName,
        Long paymentMethodId,
        String paymentMethodName,
        String remarks,
        LocalDateTime createdAt
) {}
