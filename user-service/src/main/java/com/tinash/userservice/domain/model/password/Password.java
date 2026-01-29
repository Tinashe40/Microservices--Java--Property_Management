package com.tinash.userservice.domain.model.password;

import com.tinash.cloud.utility.core.domain.ValueObject;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Objects;

/**
 * Password value object.
 * Encapsulates password hashing and validation.
 */
@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Password implements ValueObject {

    @Column(name = "password", nullable = false)
    private String value;

    @Column(name = "password_last_changed", nullable = false)
    private Instant lastChanged;

    private Password(String value, Instant lastChanged) {
        this.value = value;
        this.lastChanged = lastChanged;
    }

    /**
     * Creates a password from a hashed value.
     */
    public static Password fromHashed(String hashedValue) {
        if (hashedValue == null || hashedValue.isEmpty()) {
            throw new IllegalArgumentException("Hashed password cannot be null or empty");
        }
        return new Password(hashedValue, Instant.now());
    }

    /**
     * Creates password from already hashed value (for loading from DB).
     */
    public static Password fromHashedValue(String hashedValue, Instant lastChanged) {
        return new Password(hashedValue, lastChanged);
    }

    public String getValue() {
        return value;
    }

    public Instant getLastChanged() {
        return lastChanged;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Password password = (Password) o;
        return Objects.equals(value, password.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
