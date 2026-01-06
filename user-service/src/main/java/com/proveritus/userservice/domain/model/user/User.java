package com.proveritus.userservice.domain.model.user;

import com.proveritus.cloudutility.core.domain.BaseEntity;
import com.proveritus.userservice.domain.model.permission.Permission;
import com.proveritus.userservice.domain.model.usergroup.UserGroup;
import io.jsonwebtoken.security.Password;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * User aggregate root following DDD principles.
 * Contains business logic and invariants.
 */
@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_user_username", columnList = "username", unique = true),
        @Index(name = "idx_user_email", columnList = "email", unique = true),
        @Index(name = "idx_user_enabled_deleted", columnList = "enabled, deleted")
})
public class User extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String username;

    @Embedded
    private Email email;

    @Embedded
    private Password password;

    @Embedded
    private UserProfile profile;

    @Embedded
    private AccountStatus accountStatus;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_user_groups",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "user_group_id"),
            indexes = @Index(name = "idx_user_groups", columnList = "user_id, user_group_id")
    )
    private Set<UserGroup> userGroups = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_permissions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"),
            indexes = @Index(name = "idx_user_permissions", columnList = "user_id, permission_id")
    )
    private Set<Permission> directPermissions = new HashSet<>();

    protected User() {
    }

    /**
     * Factory method to create a new user.
     * Enforces business rules at creation time.
     */
    public static User createUser(
            String username,
            Email email,
            Password password,
            UserProfile profile) {

        User user = new User();
        user.username = username;
        user.email = email;
        user.password = password;
        user.profile = profile;
        user.accountStatus = AccountStatus.createActive();

        // Domain event
        user.registerEvent(new UserCreatedEvent(user.id, username, email.value()));

        return user;
    }

    /**
     * Changes user password with validation.
     */
    public void changePassword(Password oldPassword, Password newPassword, PasswordPolicy policy) {
        if (!this.password.matches(oldPassword)) {
            throw new InvalidPasswordException("Current password is incorrect");
        }

        if (!policy.isValid(newPassword)) {
            throw new PasswordPolicyViolationException("New password doesn't meet policy requirements");
        }

        this.password = newPassword;
        this.accountStatus.markPasswordChanged(Instant.now());

        registerEvent(new PasswordChangedEvent(this.id, Instant.now()));
    }

    /**
     * Assigns user groups with authorization check.
     */
    public void assignUserGroups(Set<UserGroup> userGroups) {
        this.userGroups.clear();
        this.userGroups.addAll(userGroups);

        registerEvent(new UserGroupsAssignedEvent(this.id, userGroups.size()));
    }

    /**
     * Locks the account after failed login attempts.
     */
    public void lockAccount() {
        accountStatus.lock();
        registerEvent(new AccountLockedEvent(this.id, Instant.now()));
    }

    /**
     * Unlocks the account.
     */
    public void unlockAccount() {
        accountStatus.unlock();
        registerEvent(new AccountUnlockedEvent(this.id, Instant.now()));
    }

    /**
     * Deactivates the user account.
     */
    public void deactivate() {
        accountStatus.deactivate();
        registerEvent(new UserDeactivatedEvent(this.id, Instant.now()));
    }

    /**
     * Activates the user account.
     */
    public void activate() {
        accountStatus.activate();
        registerEvent(new UserActivatedEvent(this.id, Instant.now()));
    }

    /**
     * Gets all effective permissions (from groups + direct).
     */
    public Set<String> getAllPermissions() {
        Set<String> permissions = new HashSet<>();

        // Add permissions from groups
        userGroups.forEach(group ->
            group.getPermissions().forEach(perm ->
                permissions.add(perm.getName())));

        // Add direct permissions
        directPermissions.forEach(perm -> permissions.add(perm.getName()));

        return permissions;
    }

    // Getters
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public Email getEmail() {
        return email;
    }

    public Password getPassword() {
        return password;
    }

    public UserProfile getProfile() {
        return profile;
.    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public Set<UserGroup> getUserGroups() {
        return userGroups;
    }

    public Set<Permission> getDirectPermissions() {
        return directPermissions;
    }
}
