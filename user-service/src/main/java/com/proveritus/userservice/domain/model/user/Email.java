package com.proveritus.userservice.domain.model.user;

import com.proveritus.cloudutility.core.domain.ValueObject;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;
import java.util.regex.Pattern;

/**
 * Email value object.
 * Immutable and self-validating.
 */
@Embeddable
public class Email implements ValueObject {

    private static final Pattern EMAIL_PATTERN = 
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}$", Pattern.CASE_INSENSITIVE);

    @Column(name = "email", unique = true, nullable = false, length = 255)
    private String value;

    protected Email() {
        // JPA constructor
    }

    public Email(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }
        
        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new IllegalArgumentException("Invalid email format: " + value);
        }
        
        this.value = value.toLowerCase().trim();
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Email email = (Email) o;
        return Objects.equals(value, email.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
