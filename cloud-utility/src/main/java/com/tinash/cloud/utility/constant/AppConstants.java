package com.tinash.cloud.utility.constant;

/**
 * Global application constants.
 * This class provides a centralized location for frequently used literal values
 * to promote consistency and reduce "magic strings" or numbers throughout the codebase.
 */
public final class AppConstants {

    /**
     * Private constructor to prevent instantiation.
     */
    private AppConstants() {
    }

    // --- JWT Constants ---
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String AUTHORITIES_CLAIM = "UserGroups";


    // --- Pagination Constants ---
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_SORT_BY = "id";
    public static final String DEFAULT_SORT_DIRECTION = "asc";

    // --- Validation Messages ---
    public static final String PASSWORD_POLICY_MESSAGE = "Password must be at least 8 characters long," +
            "contain at least one uppercase letter," +
            "one lowercase letter, " +
            "one digit, " +
            "and one special character.";

    // --- Rate Limiting Constants ---
    public static final long DEFAULT_RATE_LIMIT_CAPACITY = 100; // requests per period
    public static final long DEFAULT_RATE_LIMIT_PERIOD_MINUTES = 1; // minutes

    // --- API Paths ---
    public static final String API_VERSION_V1 = "/api/v1";

    // --- Date and Time ---
    public static final String ISO_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    // --- Validations ---
    public static final String EMAIL_REGEX = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    public static final String PHONE_NUMBER_REGEX = "^[0-9]{10}$";

}
