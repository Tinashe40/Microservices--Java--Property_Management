package com.proveritus.cloudutility.security;

public final class UserGroup {
    private UserGroup() {}

    public static final String ADMIN = "ADMIN";
    public static final String SUPER_ADMIN = "SUPER_ADMIN";
    public static final String USER = "USER";
    public static final String ADMINISTRATOR = "ADMINISTRATOR";
    public static final String PROPERTY_MANAGER = "PROPERTY_MANAGER";
    public static final String MAINTENANCE_STAFF = "MAINTENANCE_STAFF";
    public static final String TENANT= "TENANT";
    public static final String PROPERTY_OWNER = "PROPERTY_OWNER";
    public static final String CARE_TAKER = "CARE_TAKER";
    public static final String HAS_ANY_ROLE_ADMIN_SUPER_ADMIN = "hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')";
}
