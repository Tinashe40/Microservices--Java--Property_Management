package com.tinash.cloud.utility.cache;

/**
 * A utility class that holds constant names for all caches used in the application.
 * This helps prevent typos and keeps a centralized registry of cache regions.
 */
public final class CacheNames {

    /**
     * Private constructor to prevent instantiation.
     */
    private CacheNames() {
    }

    public static final String USERS_CACHE = "users";
    public static final String ROLES_CACHE = "roles";
    public static final String PROPERTIES_CACHE = "properties";
    public static final String FLOORS_CACHE = "floors";
    public static final String UNITS_CACHE = "units";
    public static final String PERMISSIONS_CACHE = "permissions";

    // Add other cache names here as the application grows
}
