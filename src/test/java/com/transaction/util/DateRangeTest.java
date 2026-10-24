package com.transaction.util;

import com.transaction.exception.BadRequestException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class DateRangeTest {

    @Test
    void monthCoversWholeDaysEndExclusive() {
        DateRange range = DateRange.resolve(LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 31), null, null);

        assertEquals(LocalDateTime.of(2026, 10, 1, 0, 0), range.start());
        assertEquals(LocalDateTime.of(2026, 11, 1, 0, 0), range.end());
    }

    @Test
    void singleDay() {
        DateRange range = DateRange.resolve(LocalDate.of(2026, 10, 22), LocalDate.of(2026, 10, 22), null, null);

        assertEquals(LocalDateTime.of(2026, 10, 22, 0, 0), range.start());
        assertEquals(LocalDateTime.of(2026, 10, 23, 0, 0), range.end());
    }

    @Test
    void datesTakePrecedenceOverLegacyTimestamps() {
        LocalDateTime legacy = LocalDateTime.of(2026, 9, 30, 18, 30);
        DateRange range = DateRange.resolve(LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 1), legacy, legacy);

        assertEquals(LocalDateTime.of(2026, 10, 1, 0, 0), range.start());
    }

    @Test
    void fallsBackToLegacyTimestamps() {
        LocalDateTime start = LocalDateTime.of(2026, 10, 22, 0, 0);
        LocalDateTime end = LocalDateTime.of(2026, 10, 22, 23, 59, 59);
        DateRange range = DateRange.resolve(null, null, start, end);

        assertEquals(start, range.start());
        assertEquals(end, range.end());
    }

    @Test
    void rejectsHalfOrReversedRange() {
        assertThrows(BadRequestException.class, () -> DateRange.resolve(LocalDate.of(2026, 10, 1), null, null, null));
        assertThrows(BadRequestException.class,
                () -> DateRange.resolve(LocalDate.of(2026, 10, 2), LocalDate.of(2026, 10, 1), null, null));
    }

    @Test
    void requireBoundsRejectsMissingRange() {
        assertThrows(BadRequestException.class, () -> DateRange.resolve(null, null, null, null).requireBounds());
    }
}
