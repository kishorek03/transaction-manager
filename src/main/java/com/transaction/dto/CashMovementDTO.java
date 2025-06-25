package com.transaction.dto;

import com.transaction.entity.MovementType;

import java.math.BigDecimal;

public record CashMovementDTO(
    BigDecimal amount,
    MovementType type,
    String reason,
    String userId
) {}
