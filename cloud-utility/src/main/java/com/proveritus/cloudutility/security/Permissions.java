package com.proveritus.cloudutility.security;

public final class Permissions {
    private Permissions() {
    }

    public static final String PROPERTY_CREATE = "property:create";
    public static final String PROPERTY_READ = "property:read";
    public static final String PROPERTY_UPDATE = "property:update";
    public static final String PROPERTY_DELETE = "property:delete";
    public static final String PROPERTY_COUNT_READ = "property:count:read";
    public static final String SYSTEM_STATS_READ = "system:stats:read";

    public static final String LEASING_AGENT_READ = "leasing_agent:read";
    public static final String LEASING_AGENT_UPDATE_OCCUPANCY = "leasing_agent:update_occupancy";

    public static final String MAINTENANCE_STAFF_READ = "maintenance_staff:read";

    public static final String FLOOR_CREATE = "floor:create";
    public static final String FLOOR_READ = "floor:read";
    public static final String FLOOR_UPDATE = "floor:update";
    public static final String FLOOR_DELETE = "floor:delete";

    public static final String UNIT_CREATE = "unit:create";
    public static final String UNIT_READ = "unit:read";
    public static final String UNIT_UPDATE = "unit:update";
    public static final String UNIT_DELETE = "unit:delete";

    public static final String USER_CREATE = "user:create";
    public static final String USER_READ = "user:read";
    public static final String USER_UPDATE = "user:update";
    public static final String USER_DELETE = "user:delete";
    public static final String USER_RESET_PASSWORD = "user:reset-password";
    public static final String USER_ASSIGN_ROLES = "user:assign-roles";
    public static final String USER_DEACTIVATE = "user:deactivate";

    public static final String ROLE_CREATE = "role:create";
    public static final String ROLE_READ = "role:read";
    public static final String ROLE_UPDATE = "role:update";
    public static final String ROLE_DELETE = "role:delete";

    public static final String PERMISSION_CREATE = "permission:create";
    public static final String PERMISSION_READ = "permission:read";
    public static final String PERMISSION_UPDATE = "permission:update";
    public static final String PERMISSION_DELETE = "permission:delete";
    public static final String PERMISSION_ASSIGN_ROLES = "permission:assign-roles";
    public static final String PERMISSION_DEACTIVATE = "permission:deactivate";
}
