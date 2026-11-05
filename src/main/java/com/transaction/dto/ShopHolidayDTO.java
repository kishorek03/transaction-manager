package com.transaction.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ShopHolidayDTO(
    Long id,

    @NotBlank(message = "Holiday title is required")
    String title,

    @NotNull(message = "Holiday date is required")
    LocalDate holidayDate,

    Boolean isShopClosed,

    String description,

    LocalDateTime createdAt
) {}
