package com.proveritus.userservice.config;

import com.proveritus.userservice.auth.domain.User;
import com.proveritus.userservice.userManager.domain.UserRepository;
import com.proveritus.userservice.userRoles.domain.Permission;
import com.proveritus.userservice.userRoles.domain.PermissionRepository;
import com.proveritus.userservice.userRoles.domain.Role;
import com.proveritus.userservice.userRoles.domain.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminUserSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final PermissionRepository permissionRepository;

    @Override
    public void run(String... args) throws Exception {
        createPermissions();
        createRoles();
        createAdminUser();
    }

    private void createPermissions() {
        createPermission("user:create");
        createPermission("user:read");
        createPermission("user:update");
        createPermission("user:delete");
        createPermission("user:reset-password");
        createPermission("user:assign-roles");
        createPermission("user:deactivate");
        createPermission("role:create");
        createPermission("role:read");
        createPermission("role:update");
        createPermission("role:delete");
        createPermission("permission:read");
    }

    private void createPermission(String name) {
        permissionRepository.findByName(name).orElseGet(() -> {
            Permission newPermission = new Permission(name);
            return permissionRepository.save(newPermission);
        });
    }

    private void createRoles() {
        Set<Permission> allPermissions = new HashSet<>(permissionRepository.findAll());

        createRole("SUPER_ADMIN", allPermissions);
        createRole("ADMIN", allPermissions);

        Set<Permission> userPermissions = new HashSet<>();
        permissionRepository.findByName("user:read").ifPresent(userPermissions::add);
        createRole("USER", userPermissions);
    }

    private void createRole(String name, Set<Permission> permissions) {
        roleRepository.findByName(name).orElseGet(() -> {
            Role newRole = new Role(name);
            newRole.setPermissions(permissions);
            return roleRepository.save(newRole);
        });
    }

    private void createAdminUser() {
        Role superAdminRole = roleRepository.findByName("SUPER_ADMIN").orElseThrow();
        userRepository.findByUsernameAndDeletedFalse("Tinashe40").ifPresentOrElse(
                user -> {
                    if (user.getRoles().stream().noneMatch(role -> role.getName().equals("SUPER_ADMIN"))) {
                        user.getRoles().add(superAdminRole);
                        userRepository.save(user);
                        log.info("Added SUPER_ADMIN role to existing user: Tinashe40");
                    }
                },
                () -> {
                    User adminUser = new User();
                    adminUser.setFirstName("Tinashe");
                    adminUser.setLastName("Mutero");
                    adminUser.setUsername("Tinashe40");
                    adminUser.setEmail("tinashemutero40@gmail.com");
                    adminUser.setPassword(passwordEncoder.encode("sudo0047"));
                    adminUser.setRoles(Set.of(superAdminRole));
                    adminUser.setEnabled(true);
                    adminUser.setAccountNonExpired(true);
                    adminUser.setAccountNonLocked(true);
                    adminUser.setCredentialsNonExpired(true);

                    userRepository.save(adminUser);
                    log.info("Super Admin user created: Tinashe40");
                }
        );
    }
}
