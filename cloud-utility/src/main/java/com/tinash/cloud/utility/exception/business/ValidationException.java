package com.tinash.cloud.utility.exception.business;

import com.tinash.cloud.utility.exception.base.BusinessException;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Exception thrown for validation failures.
 */
public class ValidationException extends BusinessException {
    
    private final Map<String, List<String>> fieldErrors;
    
    public ValidationException(String message) {
        this(message, Collections.emptyMap());
    }
    
    public ValidationException(String message, Map<String, List<String>> fieldErrors) {
        super(message, "VALIDATION_ERROR");
        this.fieldErrors = fieldErrors;
    }
    
    public Map<String, List<String>> getFieldErrors() {
        return fieldErrors;
    }
    
    @Override
    public int getHttpStatus() {
        return 400;
    }
}
