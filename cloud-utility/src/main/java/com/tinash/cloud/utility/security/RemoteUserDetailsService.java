package com.tinash.cloud.utility.security;

import com.tinash.cloud.utility.dto.common.UserDto;
import com.tinash.cloud.utility.security.model.CustomPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Abstract service for loading user details from a remote source.
 * Microservices should extend this and implement getUserByUsername()
 * to fetch user data from their own data source.
 */
@Service
@RequiredArgsConstructor
public abstract class RemoteUserDetailsService implements UserDetailsService {

    /**
     * Fetch user data from the implementing microservice's data source.
     *
     * @param username the username to look up
     * @return UserDto containing user information
     */
    public abstract UserDto getUserByUsername(String username);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto userDTO = getUserByUsername(username);

        if (userDTO == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        return createPrincipal(userDTO);
    }

    private CustomPrincipal createPrincipal(UserDto user) {
        List<GrantedAuthority> authorities = user.getPermissions().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        Long userId = null;
        if (user.getId() != null) {
            try {
                userId = Long.parseLong(user.getId());
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(
                        "User ID must be a valid Long value, got: " + user.getId(), e);
            }
        }

        return new CustomPrincipal(
                userId,
                user.getUsername(),
                user.getEmail(),
                "",
                authorities,
                user.isEnabled(),
                user.isAccountNonExpired(),
                user.isAccountNonLocked(),
                user.isCredentialsNonExpired()
        );
    }
}