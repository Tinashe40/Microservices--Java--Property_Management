package com.tinash.cloud.utility.exception.business;

import com.tinash.cloud.utility.exception.base.BusinessException;

/**
 * Exception thrown when attempting to create a resource that already exists.
 */
public class DuplicateResourceException extends BusinessException {
    
    public DuplicateResourceException(String resourceType, String field, Object value) {
        super(
            String.format("%s already exists with %s: %s", resourceType, field, value),
            "DUPLICATE_RESOURCE"
        );
    }
    
    @Override
    public int getHttpStatus() {
        return 409; // Conflict
    }
}
