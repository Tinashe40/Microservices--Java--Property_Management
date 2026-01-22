package com.tinash.cloud.utility.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * Utility class for common date and time manipulation operations.
 */
public final class DateUtils {

    private DateUtils() {
        // Prevent instantiation
    }

    /**
     * Converts a {@link Date} object to {@link LocalDateTime}.
     *
     * @param dateToConvert The Date object to convert.
     * @return The converted LocalDateTime object.
     */
    public static LocalDateTime convertToLocalDateTime(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    /**
     * Converts a {@link LocalDateTime} object to {@link Date}.
     *
     * @param dateToConvert The LocalDateTime object to convert.
     * @return The converted Date object.
     */
    public static Date convertToDate(LocalDateTime dateToConvert) {
        return Date.from(dateToConvert.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Returns the current date and time as {@link LocalDateTime}.
     *
     * @return The current LocalDateTime.
     */
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }
}
