package com.tinash.cloud.utility.security.util;

import com.tinash.cloud.utility.security.authentication.UserPrincipal;
import com.tinash.cloud.utility.security.model.CustomPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

/**
 * Utility class for Spring Security operations.
 * Provides convenient methods to access current authentication context.
 */
public final class SecurityUtils {

    private SecurityUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Gets the current authenticated user's username.
     * Works with UserPrincipal, CustomPrincipal, UserDetails, or String principals.
     *
     * @return Optional containing username if authenticated, empty otherwise
     */
    public static Optional<String> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserPrincipal) {
            return Optional.of(((UserPrincipal) principal).getUsername());
        } else if (principal instanceof CustomPrincipal) {
            return Optional.of(((CustomPrincipal) principal).getUsername());
        } else if (principal instanceof UserDetails) {
            return Optional.of(((UserDetails) principal).getUsername());
        } else if (principal instanceof String) {
            return Optional.of((String) principal);
        }

        return Optional.empty();
    }

    /**
     * Legacy method name for backward compatibility.
     *
     * @deprecated Use {@link #getCurrentUser()} instead
     */
    @Deprecated
    public static Optional<String> getCurrentUserLogin() {
        return getCurrentUser();
    }

    /**
     * Gets the current authenticated user's username or returns "SYSTEM" if not authenticated.
     *
     * @return Username or "SYSTEM"
     */
    public static String getCurrentUserOrSystem() {
        return getCurrentUser().orElse("SYSTEM");
    }

    /**
     * Gets the current authenticated user's ID if available.
     * Only works with UserPrincipal or CustomPrincipal.
     *
     * @return Optional containing user ID if available
     */
    public static Optional<Long> getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomPrincipal) {
            try {
                return Optional.ofNullable(Long.valueOf(((CustomPrincipal) principal).getId()));
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        }

        return Optional.empty();
    }

    /**
     * Checks if the current user is authenticated.
     *
     * @return true if authenticated, false otherwise
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null
                && authentication.isAuthenticated()
                && !(authentication.getPrincipal() instanceof String
                && "anonymousUser".equals(authentication.getPrincipal()));
    }

    /**
     * Gets the current authentication object.
     *
     * @return Optional containing Authentication if present
     */
    public static Optional<Authentication> getCurrentAuthentication() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
    }

    /**
     * Gets the current UserPrincipal if the principal is of that type.
     *
     * @return Optional containing UserPrincipal
     */
    public static Optional<UserPrincipal> getCurrentUserPrincipal() {
        return getCurrentAuthentication()
                .map(Authentication::getPrincipal)
                .filter(principal -> principal instanceof UserPrincipal)
                .map(principal -> (UserPrincipal) principal);
    }

    /**
     * Gets the current CustomPrincipal if the principal is of that type.
     *
     * @return Optional containing CustomPrincipal
     */
    public static Optional<CustomPrincipal> getCurrentCustomPrincipal() {
        return getCurrentAuthentication()
                .map(Authentication::getPrincipal)
                .filter(principal -> principal instanceof CustomPrincipal)
                .map(principal -> (CustomPrincipal) principal);
    }

    /**
     * Checks if the current user has a specific role.
     *
     * @param role the role to check (without ROLE_ prefix)
     * @return true if user has the role
     */
    public static boolean hasRole(String role) {
        return getCurrentAuthentication()
                .map(auth -> auth.getAuthorities().stream()
                        .anyMatch(grantedAuth ->
                                grantedAuth.getAuthority().equals("ROLE_" + role)
                                        || grantedAuth.getAuthority().equals(role)))
                .orElse(false);
    }

    /**
     * Checks if the current user has any of the specified roles.
     *
     * @param roles the roles to check
     * @return true if user has any of the roles
     */
    public static boolean hasAnyRole(String... roles) {
        for (String role : roles) {
            if (hasRole(role)) {
                return true;
            }
        }
        return false;
    }
}