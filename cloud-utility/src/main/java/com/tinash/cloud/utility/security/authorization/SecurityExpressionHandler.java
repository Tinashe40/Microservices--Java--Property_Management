package com.tinash.cloud.utility.security.authorization;

import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.stereotype.Component;

@Component
public class SecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {

    private final PermissionEvaluator permissionEvaluator;

    public SecurityExpressionHandler(PermissionEvaluator permissionEvaluator) {
        this.permissionEvaluator = permissionEvaluator;
        setPermissionEvaluator(this.permissionEvaluator);
    }
}
