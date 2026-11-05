package com.transaction.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record StaffLeaveDTO(
    Long id,

    @NotBlank(message = "Staff name is required")
    String staffName,

    @NotNull(message = "Leave date is required")
    LocalDate leaveDate,

    String reason,

    LocalDateTime createdAt
) {}
