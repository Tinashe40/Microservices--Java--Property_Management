package com.proveritus.userservice.userManager.service.impl;

import com.proveritus.cloudutility.security.CustomPrincipal;
import com.proveritus.userservice.auth.domain.User;
import com.proveritus.userservice.userManager.domain.UserRepository;
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

        User user = userRepository.findByUsernameAndDeletedFalse(usernameOrEmail)
                .orElseGet(() -> userRepository.findByEmailAndDeletedFalse(usernameOrEmail)
                        .orElseThrow(() -> {
                            String errorMsg = "User not found with username or email: " + usernameOrEmail;
                            log.error(errorMsg);
                            return new UsernameNotFoundException(errorMsg);
                        }));

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

        boolean credentialsNonExpired = isCredentialsNonExpired(user);

        return new CustomPrincipal(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                authorities,
                user.isEnabled(),
                user.isAccountNonExpired(),
                user.isAccountNonLocked(),
                credentialsNonExpired
        );
    }

    private boolean isCredentialsNonExpired(User user) {
        if (user.getPasswordLastChanged() == null) {
            return true;
        }
        int passwordExpirationDays = passwordPolicyService.getPasswordPolicy().getPasswordExpirationDays();
        if (passwordExpirationDays <= 0) {
            return true;
        }
        return user.getPasswordLastChanged().plusDays(passwordExpirationDays).isAfter(LocalDateTime.now());
    }
}
