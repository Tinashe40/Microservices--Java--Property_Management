package com.proveritus.propertyservice.domain.model.property;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;

/**
 * Property name value object with validation.
 */
@Embeddable
public class PropertyName {
    
    @Column(name = "name", nullable = false, length = 200)
    private String value;

    protected PropertyName() {
    }

    public PropertyName(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Property name cannot be empty");
        }
        if (value.length() > 200) {
            throw new IllegalArgumentException("Property name too long (max 200 characters)");
        }
        this.value = value.trim();
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyName that = (PropertyName) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
