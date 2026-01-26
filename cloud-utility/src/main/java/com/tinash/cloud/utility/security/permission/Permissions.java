package com.tinash.cloud.utility.security.permission;

public final class Permissions {
    private Permissions() {
    }

    public static final class Property {
        private Property() {}
        public static final String CREATE = "property:create";
        public static final String READ = "property:read";
        public static final String UPDATE = "property:update";
        public static final String DELETE = "property:delete";
        public static final String COUNT_READ = "property:count:read";
    }

    public static final class System {
        private System() {}
        public static final String STATS_READ = "system:stats:read";
        public static final String STATS_WRITE = "system:stats:write";
        public static final String STATS_CREATE = "system:stats:create";
        public static final String STATS_UPDATE = "system:stats:update";
        public static final String STATS_DELETE = "system:stats:delete";
        public static final String STATS_COUNT_READ = "system:stats:count:read";
    }

    public static final class LeasingAgent {
        private LeasingAgent() {}
        public static final String READ = "leasing_agent:read";
        public static final String UPDATE_OCCUPANCY = "leasing_agent:update_occupancy";
    }

    public static final class MaintenanceStaff {
        private MaintenanceStaff() {}
        public static final String READ = "maintenance_staff:read";
        public static final String WRITE = "maintenance_staff:write";
        public static final String CREATE = "maintenance_staff:create";
        public static final String DELETE = "maintenance_staff:delete";
        public static final String COUNT_READ = "maintenance_staff:count:read";
        public static final String UPDATE = "maintenance_staff:update";
    }

    public static final class Floor {
        private Floor() {}
        public static final String CREATE = "floor:create";
        public static final String READ = "floor:read";
        public static final String UPDATE = "floor:update";
        public static final String DELETE = "floor:delete";
    }

    public static final class Unit {
        private Unit() {}
        public static final String CREATE = "unit:create";
        public static final String READ = "unit:read";
        public static final String UPDATE = "unit:update";
        public static final String DELETE = "unit:delete";
    }

    public static final class User {
        private User() {}
        public static final String CREATE = "user:create";
        public static final String READ = "user:read";
        public static final String UPDATE = "user:update";
        public static final String DELETE = "user:delete";
        public static final String RESET_PASSWORD = "user:reset-password";
        public static final String ASSIGN_GROUPS = "user:assign-groups";
        public static final String DEACTIVATE = "user:deactivate";
        public static final String BLOCK = "user:block";
        public static final String UNBLOCK = "user:unblock";
    }

    public static final class UserGroup {
        private UserGroup() {}
        public static final String CREATE = "user_group:create";
        public static final String READ = "user_group:read";
        public static final String UPDATE = "user_group:update";
        public static final String DELETE = "user_group:delete";
        public static final String ASSIGN_PERMISSIONS = "user_group:assign-permissions";
        public static final String DEACTIVATE = "user_group:deactivate";
        public static final String UNASSIGN_PERMISSIONS = "user_group:unassign-permissions";
        public static final String ASSIGN_GROUPS = "user_group:assign-groups";
        public static final String DEACTIVATE_GROUPS = "user_group:deactivate-groups";

    }

    public static final class Permission {
        private Permission() {}
        public static final String CREATE = "permission:create";
        public static final String READ = "permission:read";
        public static final String UPDATE = "permission:update";
        public static final String DELETE = "permission:delete";
        public static final String ASSIGN_GROUPS = "permission:assign-groups";
        public static final String DEACTIVATE = "permission:deactivate";
        public static final String UNASSIGN_PERMISSIONS = "permission:unassign-permissions";
        public static final String UNASSIGN_GROUPS = "permission:unassign-groups";
    }

    public static final class PasswordPolicy {
        private PasswordPolicy() {}
        public static final String CREATE_PASSWORD_POLICY = "passwordPolicy:create-password-policy";
        public static final String VIEW_PASSWORD_POLICY = "passwordPolicy:view-password-policy";
        public static final String UPDATE_PASSWORD_POLICY = "passwordPolicy:update-password-policy";
        public static final String DELETE_PASSWORD_POLICY = "passwordPolicy:delete-password-policy";
        public static final String RESET_PASSWORD_POLICY = "passwordPolicy:reset-password-policy";

    }
    public static final class Password {
        private Password() {}
        public static final String CREATE = "password:create";
        public static final String READ = "password:read";
        public static final String UPDATE = "password:update";
        public static final String RESET_PASSWORD = "password:reset-password";

    }
}
