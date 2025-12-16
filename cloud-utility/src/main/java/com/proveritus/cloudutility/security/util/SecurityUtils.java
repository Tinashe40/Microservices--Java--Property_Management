package com.proveritus.cloudutility.security.util;

import com.proveritus.cloudutility.dto.UserDTO;
import com.proveritus.cloudutility.security.CustomPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.stream.Collectors;

public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static Optional<CustomPrincipal> getCurrentUserPrincipal() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .filter(principal -> principal instanceof CustomPrincipal)
                .map(CustomPrincipal.class::cast);
    }

    public static Optional<UserDTO> getCurrentUserDTO() {
        return getCurrentUserPrincipal().map(principal -> {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(principal.getId());
            userDTO.setUsername(principal.getUsername());
            userDTO.setEmail(principal.getEmail());
            userDTO.setUserGroups(principal.getAuthorities().stream()
                    .map(a -> a.getAuthority())
                    .collect(Collectors.toSet()));
            userDTO.setEnabled(principal.isEnabled());
            userDTO.setAccountNonExpired(principal.isAccountNonExpired());
            userDTO.setAccountNonLocked(principal.isAccountNonLocked());
            userDTO.setCredentialsNonExpired(principal.isCredentialsNonExpired());
            return userDTO;
        });
    }

    public static Optional<Long> getCurrentUserId() {
        return getCurrentUserPrincipal().map(CustomPrincipal::getId);
    }

    public static Optional<String> getCurrentUsername() {
        return getCurrentUserPrincipal().map(CustomPrincipal::getUsername);
    }

    public static Optional<String> getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return Optional.of(bearerToken.substring(7));
        }
        return Optional.empty();
    }
}
