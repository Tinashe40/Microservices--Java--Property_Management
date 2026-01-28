package com.proveritus.userservice.userManager.service.impl;

import com.tinash.cloud.utility.security.model.CustomPrincipal;
import com.proveritus.userservice.domain.model.user.User;
import com.proveritus.userservice.domain.repository.UserRepository;
import com.proveritus.userservice.passwordManager.service.PasswordPolicyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordPolicyService passwordPolicyService;

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
    public UserDetails loadUserById(Long id) {
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
                user.getPasswordHash(), // Use getPasswordHash() from the Password embedded object
                authorities,
                user.isEnabled(),
                user.isAccountNonExpired(),
                user.isAccountNonLocked(),
                user.isCredentialsNonExpired() // Delegate to User's method
        );
    }

    private boolean isCredentialsNonExpired(User user) {
        // This logic is now delegated to the User entity's isCredentialsNonExpired method,
        // which itself delegates to AccountStatus.
        // We can simplify this method or remove it if not needed elsewhere.
        return user.isCredentialsNonExpired();
    }}
