package com.tinash.userservice.userManager.service.impl;

import com.tinash.cloud.utility.security.model.CustomPrincipal;
import com.tinash.userservice.domain.model.user.User;
import com.tinash.userservice.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        log.debug("Loading user by username or email: {}", usernameOrEmail);

        // Try to find by email first
        Optional<User> userOptional = userRepository.findByEmail_ValueAndDeletedFalse(usernameOrEmail);

        if (userOptional.isEmpty()) {
            userOptional = userRepository.findByUserProfile_UsernameAndDeletedFalse(usernameOrEmail);
        }

        User user = userOptional.orElseThrow(() -> {
            String errorMsg = "User not found with username or email: " + usernameOrEmail;
            log.error(errorMsg);
            return new UsernameNotFoundException(errorMsg);
        });

        return createPrincipal(user);
    }

    @Transactional(readOnly = true)
    public UserDetails loadUserById(String id) {
        log.debug("Loading user by ID: {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    String errorMsg = "User not found with id: " + id;
                    log.error(errorMsg);
                    return new UsernameNotFoundException(errorMsg);
                });

        return createPrincipal(user);
    }

    private CustomPrincipal createPrincipal(User user) {
        List<GrantedAuthority> authorities = user.getUserGroups().stream()
                .flatMap(userGroup -> userGroup.getPermissions().stream())
                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                .collect(Collectors.toList());

        authorities.addAll(user.getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                .collect(Collectors.toList()));

        // All these properties are now directly accessible via User getters that delegate to embedded objects
        return new CustomPrincipal(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPasswordValue(),
                authorities,
                user.isEnabled(),
                user.isAccountNonExpired(),
                user.isAccountNonLocked(),
                user.isCredentialsNonExpired() // Delegate to User's method
        );
    }
}
