package com.tinash.cloud.utility.util.string;

/**
 * Utility class for common string manipulation operations.
 */
public final class StringUtils {

    private StringUtils() {
        // Prevent instantiation
    }

    /**
     * Checks if a string is null, empty, or consists only of whitespace characters.
     *
     * @param str The string to check.
     * @return true if the string is null, empty, or blank; false otherwise.
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Checks if a string is not null, not empty, and does not consist only of whitespace characters.
     *
     * @param str The string to check.
     * @return true if the string is not null, not empty, and not blank; false otherwise.
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * Capitalizes the first letter of a given string.
     *
     * @param str The string to capitalize.
     * @return The capitalized string, or null if the input is null.
     */
    public static String capitalizeFirstLetter(String str) {
        if (isBlank(str)) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}