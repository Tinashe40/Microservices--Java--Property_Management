package com.proveritus.cloudutility.security;

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
    }

    public static final class LeasingAgent {
        private LeasingAgent() {}
        public static final String READ = "leasing_agent:read";
        public static final String UPDATE_OCCUPANCY = "leasing_agent:update_occupancy";
    }

    public static final class MaintenanceStaff {
        private MaintenanceStaff() {}
        public static final String READ = "maintenance_staff:read";
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
    }

    public static final class UserGroup {
        private UserGroup() {}
        public static final String CREATE = "user_group:create";
        public static final String READ = "user_group:read";
        public static final String UPDATE = "user_group:update";
        public static final String DELETE = "user_group:delete";
        public static final String ASSIGN_PERMISSIONS = "user_group:assign-permissions";
    }

    public static final class Permission {
        private Permission() {}
        public static final String CREATE = "permission:create";
        public static final String READ = "permission:read";
        public static final String UPDATE = "permission:update";
        public static final String DELETE = "permission:delete";
        public static final String ASSIGN_GROUPS = "permission:assign-groups";
        public static final String DEACTIVATE = "permission:deactivate";
    }
}