package com.proveritus.userservice.security;

public final class Roles {
    private Roles() {}

    public static final String ADMIN = "ADMIN";
    public static final String SUPER_ADMIN = "SUPER_ADMIN";

    public static final String HAS_ANY_ROLE_ADMIN_SUPER_ADMIN = "hasAnyAuthority('ROLE_ADMIN', 'ROLE_SUPER_ADMIN')";
}
