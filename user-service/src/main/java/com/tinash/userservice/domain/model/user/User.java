package com.tinash.userservice.domain.model.user;

import com.tinash.userservice.domain.model.IdPrefix;
import com.tinash.userservice.domain.model.password.Password;
import com.tinash.userservice.domain.model.permission.Permission;
import com.tinash.userservice.domain.model.usergroup.UserGroup;
import com.tinash.userservice.shared.domain.id.HibernateIdGeneratorAdapter;
import com.tinash.cloud.utility.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_username", columnList = "username", unique = true),
        @Index(name = "idx_email", columnList = "email", unique = true),
        @Index(name = "idx_enabled", columnList = "enabled"),
        @Index(name = "idx_account_locked", columnList = "account_non_locked")
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
            parameters = @Parameter(name = "prefix", value = IdPrefix.USER)
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RefreshToken> refreshTokens;

    @Transient
    private transient UserCreatedEvent domainEvent;

    // Constructor for creating a new User
    public User(String username, String emailString, String hashedPassword, String firstName, String lastName, String phoneNumber) {
        this.email = new Email(emailString);
        this.userProfile = new UserProfile(username, firstName, lastName, phoneNumber);
        this.password = Password.fromHashed(hashedPassword);
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

    public String getPasswordValue() {
        return password.getValue();
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

    public int getFailedLoginAttempts() {
        return accountStatus.getFailedLoginAttempts();
    }

    public LocalDateTime getLastLoginAt() {
        return accountStatus.getLastLogin() != null ? LocalDateTime.ofInstant(accountStatus.getLastLogin(), java.time.ZoneOffset.UTC) : null;
    }

    // ========== Domain Behavior ==========

    public void changePassword(String newHashedPassword) {
        this.password = Password.fromHashed(newHashedPassword);
        this.accountStatus.markPasswordChanged(java.time.Instant.now());
        this.accountStatus.unlock();
    }

    public void recordFailedLoginAttempt() {
        this.accountStatus.recordFailedLogin();
    }

    public void unlockAccount() {
        this.accountStatus.unlock();
    }

    public void recordSuccessfulLogin() {
        this.accountStatus.recordSuccessfulLogin();
    }

    public void expireCredentials() {
        this.accountStatus.expireCredentials();
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