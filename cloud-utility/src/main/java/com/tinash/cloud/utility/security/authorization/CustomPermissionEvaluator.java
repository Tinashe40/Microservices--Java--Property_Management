package com.tinash.cloud.utility.security.authorization;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Slf4j
@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication authentication,
                                 Object targetDomainObject,
                                 Object permission) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        log.debug("Evaluating permission '{}' for user '{}' on object '{}'",
                permission, authentication.getName(), targetDomainObject);

        String requiredPermission = permission.toString().toUpperCase();

        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals(requiredPermission)
                        || authority.equals("ROLE_ADMIN"));
    }

    @Override
    public boolean hasPermission(Authentication authentication,
                                 Serializable targetId,
                                 String targetType,
                                 Object permission) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        log.debug("Evaluating permission '{}' for user '{}' on {} with id '{}'",
                permission, authentication.getName(), targetType, targetId);

        String requiredPermission = String.format("%s_%s",
                targetType.toUpperCase(),
                permission.toString().toUpperCase());

        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authority -> authority.equals(requiredPermission)
                        || authority.equals("ROLE_ADMIN"));
    }
}