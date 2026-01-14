package com.proveritus.userservice.domain.model.user;

import com.proveritus.userservice.domain.event.UserCreatedEvent;
import com.proveritus.cloudutility.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.List;

@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_username", columnList = "username", unique = true),
        @Index(name = "idx_email", columnList = "email", unique = true),
        @Index(name = "idx_enabled", columnList = "enabled"),
        @Index(name = "idx_account_locked", columnList = "account_locked")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(length = 20)
    private String phoneNumber;

    private LocalDateTime passwordLastChanged;

    @Column(nullable = false)
    private boolean enabled = true;

    @Column(nullable = false)
    private boolean accountNonExpired = true;

    @Column(nullable = false, name = "account_locked")
    private boolean accountNonLocked = true;

    @Column(nullable = false)
    private boolean credentialsNonExpired = true;

    private LocalDateTime lastLoginAt;

    private Integer failedLoginAttempts = 0;

    private LocalDateTime lockedUntil;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "user_user_groups",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "user_group_id"),
            indexes = {
                    @Index(name = "idx_user_user_groups_user_id", columnList = "user_id"),
                    @Index(name = "idx_user_user_groups_group_id", columnList = "user_group_id")
            }
    )
    private Set<UserGroup> userGroups = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "user_permissions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"),
            indexes = {
                    @Index(name = "idx_user_permissions_user_id", columnList = "user_id"),
                    @Index(name = "idx_user_permissions_permission_id", columnList = "permission_id")
            }
    )
    private Set<Permission> permissions = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<UserToken> tokens;

    @Transient
    private transient UserCreatedEvent domainEvent;

    // ========== Domain Behavior ==========

    public void registerUser(String firstName, String lastName, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.passwordLastChanged = LocalDateTime.now();
        this.enabled = true;
        this.accountNonLocked = true;
        this.credentialsNonExpired = true;

        // Publish domain event
        this.domainEvent = new UserCreatedEvent(this);
    }

    public void changePassword(String newEncodedPassword) {
        this.password = newEncodedPassword;
        this.passwordLastChanged = LocalDateTime.now();
        this.credentialsNonExpired = true;
        this.failedLoginAttempts = 0;
        this.lockedUntil = null;
    }

    public void recordFailedLoginAttempt() {
        this.failedLoginAttempts = (this.failedLoginAttempts == null ? 0 : this.failedLoginAttempts) + 1;
    }

    public void lockAccount(LocalDateTime lockUntil) {
        this.accountNonLocked = false;
        this.lockedUntil = lockUntil;
    }

    public void unlockAccount() {
        this.accountNonLocked = true;
        this.failedLoginAttempts = 0;
        this.lockedUntil = null;
    }

    public void recordSuccessfulLogin() {
        this.failedLoginAttempts = 0;
        this.lastLoginAt = LocalDateTime.now();
        this.lockedUntil = null;
    }

    public void expireCredentials() {
        this.credentialsNonExpired = false;
    }

    public void deactivate() {
        this.enabled = false;
    }

    public void activate() {
        this.enabled = true;
    }

    public boolean isAccountLocked() {
        if (!accountNonLocked) {
            if (lockedUntil != null && LocalDateTime.now().isAfter(lockedUntil)) {
                unlockAccount();
                return false;
            }
            return true;
        }
        return false;
    }

    public boolean isCredentialsExpired() {
        return !credentialsNonExpired;
    }

    public void assignUserGroup(UserGroup userGroup) {
        this.userGroups.add(userGroup);
    }

    public void removeUserGroup(UserGroup userGroup) {
        this.userGroups.remove(userGroup);
    }

    public void assignPermission(Permission permission) {
        this.permissions.add(permission);
    }

    public void removePermission(Permission permission) {
        this.permissions.remove(permission);
    }

    public UserCreatedEvent getDomainEvent() {
        return this.domainEvent;
    }

    public void clearDomainEvent() {
        this.domainEvent = null;
    }
}