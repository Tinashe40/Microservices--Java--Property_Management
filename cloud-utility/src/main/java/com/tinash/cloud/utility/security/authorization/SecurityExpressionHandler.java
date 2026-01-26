package com.tinash.cloud.utility.security.authorization;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.stereotype.Component;

/**
 * Custom Security Expression Handler for method-level security.
 * FIXED: Use Spring Security's PermissionEvaluator interface instead of custom one.
 */

@Component
public class SecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {

    private final PermissionEvaluator permissionEvaluator;

    public SecurityExpressionHandler(
            org.springframework.security.access.PermissionEvaluator permissionEvaluator) {
        this.permissionEvaluator = permissionEvaluator;
        setPermissionEvaluator(this.permissionEvaluator);
    }
}