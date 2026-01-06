package com.proveritus.userservice.domain.model.user;

import com.proveritus.cloudutility.core.domain.ValueObject;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;

/**
 * Password value object.
 * Encapsulates password hashing and validation.
 */

@Embeddable
public class Password implements ValueObject {

    @Column(name = "password", nullable = false)
    private String hashedValue;

    @Column(name = "password_last_changed")
    private java.time.Instant lastChanged;

    protected Password() {
    }

    private Password(String hashedValue) {
        this.hashedValue = hashedValue;
        this.lastChanged = java.time.Instant.now();
    }

    /**
     * Creates a password from plain text using encoder.
     */
    public static Password fromPlainText(String plainText, PasswordEncoder encoder) {
        if (plainText == null || plainText.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        return new Password(encoder.encode(plainText));
    }

    /**
     * Creates password from already hashed value (for loading from DB).
     */
    public static Password fromHashedValue(String hashedValue) {
        return new Password(hashedValue);
    }

    /**
     * Checks if the given password matches this one.
     */
    public boolean matches(Password other, PasswordEncoder encoder) {
        return encoder.matches(other.hashedValue, this.hashedValue);
    }

    public boolean matches(Password other) {
        return Objects.equals(this.hashedValue, other.hashedValue);
    }

    public String getHashedValue() {
        return hashedValue;
    }

    public java.time.Instant getLastChanged() {
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
