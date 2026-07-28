package com.transaction.config;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateTimeUtil {

    private static final ZoneId IST = ZoneId.of("Asia/Kolkata");
    private static final ZoneId UTC = ZoneId.of("UTC");

    public static LocalDateTime utcToIst(LocalDateTime utcDateTime) {
        if (utcDateTime == null) return null;
        return ZonedDateTime.of(utcDateTime, UTC).withZoneSameInstant(IST).toLocalDateTime();
    }
}
