package com.transaction.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DailyCashBalanceDTO(
        LocalDate businessDate,
        BigDecimal amount,
        String userId
) {}
