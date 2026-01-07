package com.proveritus.cloudutility.exception.model;

import lombok.Getter;

/**
 * Validation error detail.
 */
 @Getter
public class ValidationError {
    
    private final String field;
    private final String rejectedValue;
    private final String message;

    public ValidationError(String field, String rejectedValue, String message) {
        this.field = field;
        this.rejectedValue = rejectedValue;
        this.message = message;
    }
}