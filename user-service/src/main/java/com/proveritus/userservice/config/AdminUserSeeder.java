package com.proveritus.userservice.config;

import com.proveritus.userservice.Auth.domain.User;
import com.proveritus.userservice.userManager.domain.UserRepository;
import com.proveritus.userservice.userManager.userRoles.domain.Role;
import com.proveritus.userservice.userManager.userRoles.domain.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminUserSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByEmail("tinashemutero40@gmail.com").isEmpty()) {
            Role adminRole = roleRepository.findByName("ADMIN").orElseGet(() -> {
                Role newRole = new Role("ADMIN");
                return roleRepository.save(newRole);
            });

            User adminUser = new User();
            adminUser.setFirstName("Tinashe");
            adminUser.setLastName("Mutero");
            adminUser.setUsername("Tinashe40");
            adminUser.setEmail("tinashemutero40@gmail.com");
            adminUser.setPassword(passwordEncoder.encode("sudo0047"));
            adminUser.setRoles(Set.of(adminRole));
            adminUser.setEnabled(true);
            adminUser.setAccountNonExpired(true);
            adminUser.setAccountNonLocked(true);
            adminUser.setCredentialsNonExpired(true);

            userRepository.save(adminUser);
            log.info("Admin user created: Tinashe40");
        }
    }
}
