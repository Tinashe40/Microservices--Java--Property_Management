package com.proveritus.cloudutility.validation;

import com.proveritus.cloudutility.exception.business.ValidationException;

import java.util.*;

/**
 * Generic validator interface.
 */
 @FunctionalInterface
public interface Validator<T> {
    
    /**
     * Validates an object and returns validation result.
     */
    ValidationResult validate(T object);
    
    /**
     * Validates and throws exception if validation fails.
     */
    default void validateAndThrow(T object) {
        ValidationResult result = validate(object);
        if (!result.isValid()) {
            throw new ValidationException("Validation failed", result.getErrors());
        }
    }
    
    /**
     * Chains validators together.
     */
    default Validator<T> and(Validator<T> other) {
        return object -> {
            ValidationResult result1 = this.validate(object);
            ValidationResult result2 = other.validate(object);
            return result1.merge(result2);
        };
    }
}