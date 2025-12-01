package com.proveritus.userservice.config;

import com.proveritus.cloudutility.security.Permissions;
import com.proveritus.userservice.auth.domain.User;
import com.proveritus.userservice.userManager.domain.UserRepository;
import com.proveritus.userservice.userRoles.domain.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminUserSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final PermissionRepository permissionRepository;

    @Override
    public void run(String... args) {
        try {
            createPermissions();
            createAdminRole();
            createAdminUser();
            log.info("Admin user seeding completed successfully");

        } catch (Exception e) {
            log.error("Error during admin user seeding", e);
        }
    }

    private void createPermissions() {

        List<String> permissionNames = List.of(
                "user:create", "user:read", "user:update", "user:delete",
                "user:reset-password", "user:assign-roles", "user:deactivate",
                "role:create", "role:read", "role:update", "role:delete",
                "permission:create", "permission:read", "permission:update",
                "permission:delete", "permission:assign-roles", "permission:deactivate",
                "property:create", "property:read", "property:update", "property:delete",
                "property:count:read", "system:stats:read",
                "leasing_agent:read", "leasing_agent:update_occupancy",
                "maintenance_staff:read",
                "floor:create", "floor:read", "floor:update", "floor:delete",
                "unit:create", "unit:read", "unit:update", "unit:delete"
        );

        permissionNames.forEach(this::createPermissionIfNotExists);
        log.info("Created {} permissions", permissionNames.size());
    }

    private void createPermissionIfNotExists(String name) {

        permissionRepository.findByName(name).orElseGet(() -> {

            Permission permission = new Permission(name);

            Permission saved = permissionRepository.save(permission);

            log.debug("Created permission: {}", name);

            return saved;

        });

    }



    private void createAdminRole() {

        Set<Permission> allPermissions = new HashSet<>(permissionRepository.findAll());



        roleRepository.findByName("ADMIN").orElseGet(() -> {

            Role adminRole = new Role("ADMIN");

            adminRole.setPermissions(allPermissions);

            Role saved = roleRepository.save(adminRole);

            log.info("Created ADMIN role with {} permissions", allPermissions.size());

            return saved;

        });

    }



    private void createAdminUser() {

        Role adminRole = roleRepository.findByName("ADMIN")

                .orElseThrow(() -> new IllegalStateException("ADMIN role not found"));



        userRepository.findByUsernameAndDeletedFalse("Tinashe40").ifPresentOrElse(

                user -> {

                    if (user.getRoles().stream().noneMatch(role -> "ADMIN".equals(role.getName()))) {

                        user.getRoles().add(adminRole);

                        userRepository.save(user);

                        log.info("Added ADMIN role to existing user: {}", user.getUsername());

                    } else {

                        log.info("User {} already has ADMIN role", user.getUsername());

                    }

                },

                () -> {

                    User adminUser = new User();

                    adminUser.setFirstName("Tinashe");

                    adminUser.setLastName("Mutero");
                    adminUser.setUsername("Tinashe40");
                    adminUser.setEmail("tinashemutero40@gmail.com");
                    adminUser.setPhoneNumber("0785529900");
                    adminUser.setPassword(passwordEncoder.encode("sudo0047"));
                    adminUser.setRoles(Set.of(adminRole));
                    adminUser.setEnabled(true);
                    adminUser.setAccountNonExpired(true);
                    adminUser.setAccountNonLocked(true);

                    adminUser.setCredentialsNonExpired(true);



                    userRepository.save(adminUser);

                    log.info("Created admin user: {}", adminUser.getUsername());

                }

        );

    }

}


