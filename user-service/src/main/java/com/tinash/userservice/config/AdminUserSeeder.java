package com.tinash.userservice.config;

import com.tinash.userservice.domain.model.user.User;
import com.tinash.userservice.domain.repository.UserRepository;
import com.tinash.userservice.domain.model.permission.Permission;
import com.tinash.userservice.domain.model.usergroup.UserGroup;
import com.tinash.userservice.domain.repository.PermissionRepository;
import com.tinash.userservice.domain.repository.UserGroupRepository;
import com.tinash.cloud.utility.security.password.CustomPasswordEncoder;
import com.tinash.cloud.utility.security.permission.Permissions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminUserSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final UserGroupRepository userGroupRepository;
    private final CustomPasswordEncoder customPasswordEncoder;
    private final PermissionRepository permissionRepository;

    @Override
    public void run(String... args) {
        try {
            createPermissions();
            createAdminUserGroup();
            createAdminUser();
            log.info("Admin user seeding completed successfully");

        } catch (Exception e) {
            log.error("Error during admin user seeding", e);
        }
    }

    private void createPermissions() {
        List<String> permissionNames = Arrays.asList(
                Permissions.User.CREATE, Permissions.User.READ, Permissions.User.UPDATE, Permissions.User.DELETE,
                Permissions.User.RESET_PASSWORD, Permissions.User.ASSIGN_GROUPS, Permissions.User.DEACTIVATE,
                Permissions.UserGroup.CREATE, Permissions.UserGroup.READ, Permissions.UserGroup.UPDATE, Permissions.UserGroup.DELETE,
                Permissions.Permission.CREATE, Permissions.Permission.READ, Permissions.Permission.UPDATE,
                Permissions.Permission.DELETE, Permissions.Permission.ASSIGN_GROUPS, Permissions.Permission.DEACTIVATE,
                Permissions.Property.CREATE, Permissions.Property.READ, Permissions.Property.UPDATE, Permissions.Property.DELETE,
                Permissions.Property.COUNT_READ, Permissions.System.STATS_READ,
                Permissions.LeasingAgent.READ, Permissions.LeasingAgent.UPDATE_OCCUPANCY,
                Permissions.MaintenanceStaff.READ,
                Permissions.Floor.CREATE, Permissions.Floor.READ, Permissions.Floor.UPDATE, Permissions.Floor.DELETE,
                Permissions.Unit.CREATE, Permissions.Unit.READ, Permissions.Unit.UPDATE, Permissions.Unit.DELETE
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



    private void createAdminUserGroup() {

        Set<Permission> allPermissions = new HashSet<>(permissionRepository.findAll());

        userGroupRepository.findByName("ADMIN").orElseGet(() -> {

            UserGroup adminUserGroup = new UserGroup("ADMIN");

            adminUserGroup.setPermissions(allPermissions);

            UserGroup saved = userGroupRepository.save(adminUserGroup);

            log.info("Created ADMIN user group with {} permissions", allPermissions.size());

            return saved;

        });

    }



    private void createAdminUser() {

        UserGroup adminUserGroup = userGroupRepository.findByName("ADMIN")

                .orElseThrow(() -> new IllegalStateException("ADMIN user group not found"));



        userRepository.findByUserProfile_UsernameAndDeletedFalse("Tinashe40").ifPresentOrElse(

                user -> {

                    if (user.getUserGroups().stream().noneMatch(userGroup -> "ADMIN".equals(userGroup.getName()))) {

                        user.getUserGroups().add(adminUserGroup);

                        userRepository.save(user);

                        log.info("Added ADMIN user group to existing user: {}", user.getUsername());

                    } else {

                        log.info("User {} already has ADMIN user group", user.getUsername());

                    }

                },

                () -> {
                    User adminUser = new User(
                            "tinashem",
                            "tinashemutero40@gmail.com",
                            passwordEncoder.encode("sudo0047"),
                            "Tinashe",
                            "Mutero",
                            "0785529900"
                    );
                    adminUser.assignUserGroup(adminUserGroup);

                    userRepository.save(adminUser);

                    log.info("Created admin user: {}", adminUser.getUsername());

                }

        );

    }

}
