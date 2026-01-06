package com.proveritus.userservice.domain.model.user;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.Instant;

/**
 * Account status value object.
 */
@Embeddable
public class AccountStatus {

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Column(name = "account_non_locked", nullable = false)
    private boolean accountNonLocked;

    @Column(name = "account_non_expired", nullable = false)
    private boolean accountNonExpired;

    @Column(name = "credentials_non_expired", nullable = false)
    private boolean credentialsNonExpired;

    @Column(name = "failed_login_attempts")
    private int failedLoginAttempts;

    @Column(name = "last_login")
    private Instant lastLogin;

    protected AccountStatus() {
    }

    public static AccountStatus createActive() {
        AccountStatus status = new AccountStatus();
        status.enabled = true;
        status.accountNonLocked = true;
        status.accountNonExpired = true;
        status.credentialsNonExpired = true;
        status.failedLoginAttempts = 0;
        return status;
    }

    public void lock() {
        this.accountNonLocked = false;
    }

    public void unlock() {
        this.accountNonLocked = true;
        this.failedLoginAttempts = 0;
    }

    public void deactivate() {
        this.enabled = false;
    }

    public void activate() {
        this.enabled = true;
    }

    public void recordFailedLogin() {
        this.failedLoginAttempts++;
        if (this.failedLoginAttempts >= 5) {
            lock();
        }
    }

    public void recordSuccessfulLogin() {
        this.lastLogin = Instant.now();
        this.failedLoginAttempts = 0;
    }

    public void markPasswordChanged(Instant timestamp) {
        // Reset credentials expiration on password change
        this.credentialsNonExpired = true;
    }

    // Getters
    public boolean isEnabled() {
        return enabled;
    }

    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public Instant getLastLogin() {
        return lastLogin;
    }
}
