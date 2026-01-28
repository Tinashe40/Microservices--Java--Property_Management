package com.proveritus.userservice.domain.model.user;

import com.proveritus.userservice.domain.event.UserCreatedEvent;
import com.proveritus.userservice.domain.model.permission.Permission;
import com.proveritus.userservice.shared.domain.id.HibernateIdGeneratorAdapter;
import com.tinash.cloud.utility.jpa.BaseEntity;
import com.tinash.cloud.utility.security.UserGroup;
import com.tinash.cloud.utility.security.password.CustomPasswordEncoder;
import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

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
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {
    @Id
    @GeneratedValue(generator = "ulid-generator")
    @GenericGenerator(
            name = "ulid-generator",
            type = HibernateIdGeneratorAdapter.class,
            parameters = @Parameter(name = "prefix", value = "USR-")
            )
    private String id;

    @Embedded
    private Email email;

    @Embedded
    private UserProfile userProfile;

    @Embedded
    private Password password;

    @Embedded
    private AccountStatus accountStatus;

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

    // Constructor for creating a new User
    public User(String username, String emailString, String rawPassword, String firstName, String lastName, String phoneNumber, CustomPasswordEncoder passwordEncoder) {
        this.email = new Email(emailString);
        this.userProfile = new UserProfile(username, firstName, lastName, phoneNumber, email);
        this.password = Password.fromPlainText(rawPassword, passwordEncoder);
        this.accountStatus = AccountStatus.createActive();
        // Initialize collections
        this.userGroups = new HashSet<>();
        this.permissions = new HashSet<>();
        // Publish domain event
        this.domainEvent = new UserCreatedEvent(this.getId(), this.getUsername(), this.getEmail());
    }

    // ========== Getters for embedded objects and their properties ==========

    public String getUsername() {
        return userProfile.getUsername();
    }

    public String getEmail() {
        return email.value();
    }

    public String getPasswordHash() {
        return password.getHashedValue();
    }

    public LocalDateTime getPasswordLastChanged() {
        return LocalDateTime.ofInstant(password.getLastChanged(), java.time.ZoneOffset.UTC); // Convert Instant to LocalDateTime
    }

    public String getFirstName() {
        return userProfile.getFirstName();
    }

    public String getLastName() {
        return userProfile.getLastName();
    }

    public String getPhoneNumber() {
        return userProfile.getPhoneNumber();
    }

    public boolean isEnabled() {
        return accountStatus.isEnabled();
    }

    public boolean isAccountNonExpired() {
        return accountStatus.isAccountNonExpired();
    }

    public boolean isAccountNonLocked() {
        return accountStatus.isAccountNonLocked();
    }

    public boolean isCredentialsNonExpired() {
        return accountStatus.isCredentialsNonExpired();
    }

    public Integer getFailedLoginAttempts() {
        return accountStatus.getFailedLoginAttempts();
    }

    public LocalDateTime getLastLoginAt() {
        return accountStatus.getLastLogin() != null ? LocalDateTime.ofInstant(accountStatus.getLastLogin(), java.time.ZoneOffset.UTC) : null;
    }

    public LocalDateTime getLockedUntil() {
        // AccountStatus does not directly store lockedUntil, it's inferred from accountNonLocked and failedLoginAttempts
        // This might need adjustment if a specific lockedUntil timestamp is required
        return null; // Placeholder
    }

    // ========== Domain Behavior ==========

    public void registerUser(String firstName, String lastName, String phoneNumber) {
        this.userProfile = new UserProfile(firstName, lastName, phoneNumber);
        this.accountStatus.activate(); // Ensure account is active on registration
        this.password.setLastChanged(java.time.Instant.now()); // Reset password last changed on registration
        this.accountStatus.markPasswordChanged(java.time.Instant.now()); // Mark credentials as non-expired

        // Publish domain event
        this.domainEvent = new UserCreatedEvent(this.getId(), this.getUsername(), this.getEmail());
    }

    public void changePassword(String newRawPassword, CustomPasswordEncoder customPasswordEncoder) {
        this.password = Password.fromPlainText(newRawPassword, customPasswordEncoder);
        this.accountStatus.markPasswordChanged(java.time.Instant.now());
        this.accountStatus.unlock(); // Unlock account and reset failed login attempts
    }

    public void recordFailedLoginAttempt() {
        this.accountStatus.recordFailedLogin();
    }

    public void lockAccount(LocalDateTime lockUntil) {
        this.accountStatus.lock();
        // The lockedUntil field in AccountStatus needs to be updated if this is to be used
        // For now, it will be handled implicitly by the accountStatus.isAccountNonLocked logic
    }

    public void unlockAccount() {
        this.accountStatus.unlock();
    }

    public void recordSuccessfulLogin() {
        this.accountStatus.recordSuccessfulLogin();
    }

    public void expireCredentials() {
        this.accountStatus.credentialsNonExpired = false; // Directly setting for now, consider adding a method in AccountStatus
    }

    public void deactivate() {
        this.accountStatus.deactivate();
    }

    public void activate() {
        this.accountStatus.activate();
    }

    public boolean isAccountLocked() {
        return !accountStatus.isAccountNonLocked();
    }

    public boolean isCredentialsExpired() {
        return !accountStatus.isCredentialsNonExpired();
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