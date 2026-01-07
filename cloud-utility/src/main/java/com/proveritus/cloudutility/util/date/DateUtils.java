package com.proveritus.cloudutility.util.date;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class DateUtils {

    private DateUtils() {
    }

    public static String format(Instant instant, String pattern, ZoneId zoneId) {
        return DateTimeFormatter.ofPattern(pattern).withZone(zoneId).format(instant);
    }
}