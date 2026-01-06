package com.proveritus.cloudutility.core.validation;

import com.proveritus.cloudutility.core.exception.ValidationException;

/**
 * Generic validator interface following Strategy pattern.
 *
 * @param <T> the type to validate
 */
@FunctionalInterface
public interface Validator<T> {
    
    /**
     * Validates the given object.
     *
     * @param object the object to validate
     * @throws ValidationException if validation fails
     */
    void validate(T object) throws ValidationException;
    
    /**
     * Chains this validator with another.
     *
     * @param other the other validator
     * @return a combined validator
     */
    default Validator<T> and(Validator<T> other) {
        return object -> {
            this.validate(object);
            other.validate(object);
        };
    }
}
