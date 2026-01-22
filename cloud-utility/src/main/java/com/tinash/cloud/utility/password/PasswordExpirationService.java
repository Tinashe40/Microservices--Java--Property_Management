package com.tinash.cloud.utility.password;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Service to manage password expiration policies.
 * Determines if a password has expired based on its last update time
 * and a configurable expiration period.
 */
@Service
public class PasswordExpirationService {

    @Value("${app.security.password-expiration-days:90}") // Default to 90 days
    private long passwordExpirationDays;

    /**
     * Checks if a password has expired.
     *
     * @param lastPasswordChangeDate The date and time when the password was last changed.
     * @return true if the password has expired, false otherwise.
     */
    public boolean isPasswordExpired(LocalDateTime lastPasswordChangeDate) {
        if (lastPasswordChangeDate == null) {
            // If there's no last change date, consider it expired or require change.
            // Depending on policy, might throw an exception or return true.
            return true;
        }
        LocalDateTime expirationDate = lastPasswordChangeDate.plusDays(passwordExpirationDays);
        return LocalDateTime.now().isAfter(expirationDate);
    }

    /**
     * Returns the configured password expiration period in days.
     *
     * @return The number of days after which a password expires.
     */
    public long getPasswordExpirationDays() {
        return passwordExpirationDays;
    }
}
