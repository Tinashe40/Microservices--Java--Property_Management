package com.proveritus.cloudutility.security.authorization;

import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {

    private final CustomPermissionEvaluator permissionEvaluator;

    public CustomSecurityExpressionHandler(CustomPermissionEvaluator permissionEvaluator) {
        this.permissionEvaluator = permissionEvaluator;
        setPermissionEvaluator(this.permissionEvaluator);
    }
}