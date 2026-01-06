package com.proveritus.propertyservice.domain.model.property;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Objects;

/**
 * Property ID value object.
 */
public record PropertyId(String value) {
    public PropertyId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Property ID cannot be null or empty");
        }
    }

    public static PropertyId generate() {
        return new PropertyId("PROP-" + java.util.UUID.randomUUID().toString());
    }
}
