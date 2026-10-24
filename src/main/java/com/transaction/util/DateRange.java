package com.transaction.util;

import com.transaction.exception.BadRequestException;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * A createdAt window: start inclusive, end exclusive. Timestamps are stored as IST wall-clock
 * time, so business days map directly to [from 00:00, to + 1 day 00:00).
 */
public record DateRange(LocalDateTime start, LocalDateTime end) {

    /**
     * Prefers whole business days ({@code from}/{@code to}, both inclusive). Falls back to the legacy
     * {@code start}/{@code end} timestamps, which older app versions send; either bound may then be null.
     */
    public static DateRange resolve(LocalDate from, LocalDate to, LocalDateTime start, LocalDateTime end) {
        if (from == null && to == null) {
            return new DateRange(start, end);
        }
        if (from == null || to == null) {
            throw new BadRequestException("Both 'from' and 'to' are required");
        }
        if (to.isBefore(from)) {
            throw new BadRequestException("'to' must not be before 'from'");
        }
        return new DateRange(from.atStartOfDay(), to.plusDays(1).atStartOfDay());
    }

    public DateRange requireBounds() {
        if (start == null || end == null) {
            throw new BadRequestException("Provide 'from' and 'to' dates (yyyy-MM-dd)");
        }
        return this;
    }
}
