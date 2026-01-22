package com.tinash.cloud.utility.util.date;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public final class TimeZoneConverter {

    private TimeZoneConverter() {
    }

    public static ZonedDateTime convert(Instant instant, ZoneId targetZone) {
        return instant.atZone(targetZone);
    }
}