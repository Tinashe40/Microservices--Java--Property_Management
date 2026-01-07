package com.proveritus.cloudutility.security.authentication;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFacade {

    public Optional<Authentication> getAuthentication() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
    }

    public Optional<UserPrincipal> getCurrentUser() {
        return getAuthentication()
                .filter(auth -> auth.getPrincipal() instanceof UserPrincipal)
                .map(auth -> (UserPrincipal) auth.getPrincipal());
    }

    public Optional<String> getCurrentUsername() {
        return getCurrentUser()
                .map(UserPrincipal::getUsername);
    }

    public boolean hasRole(String role) {
        return getAuthentication()
                .map(Authentication::getAuthorities)
                .map(authorities -> authorities.stream()
                        .anyMatch(auth -> auth.getAuthority().equals(role)))
                .orElse(false);
    }
}