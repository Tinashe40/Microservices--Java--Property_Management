package com.tinash.cloud.utility.security.authorization;

import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.stereotype.Component;

@Component
public class RoleHierarchy extends RoleHierarchyImpl {

    public RoleHierarchy() {
        setHierarchy("ROLE_ADMIN > ROLE_USER");
    }
}
