package com.proveritus.userservice.domain.model.user;

import com.tinash.cloud.utility.core.domain.ValueObject;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Objects;

/**
 * Password value object.
 * Encapsulates password hashing and validation.
 */
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Password implements ValueObject {

    @Column(name = "hashed_value", nullable = false)
    private String hashedValue;

    @Column(name = "last_changed", nullable = false)
    private Instant lastChanged;

    private Password(String hashedValue, Instant lastChanged) {
        this.hashedValue = hashedValue;
        this.lastChanged = lastChanged;
    }

    /**
     * Creates a password from plain text using encoder.
     */
    public static Password fromPlainText(String plainText, PasswordEncoder encoder) {
        if (plainText == null || plainText.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        return new Password(encoder.encode(plainText), Instant.now());
    }

    /**
     * Creates password from already hashed value (for loading from DB).
     */
    public static Password fromHashedValue(String hashedValue, Instant lastChanged) {
        return new Password(hashedValue, lastChanged);
    }

    /**
     * Checks if the given password matches the provided raw password using the encoder.
     */
    public boolean matches(String rawPassword, PasswordEncoder encoder) {
        return encoder.matches(rawPassword, this.hashedValue);
    }

    public String getHashedValue() {
        return hashedValue;
    }

    public Instant getLastChanged() {
        return lastChanged;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password password = (Password) o;
        return Objects.equals(hashedValue, password.hashedValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hashedValue);
    }
}
