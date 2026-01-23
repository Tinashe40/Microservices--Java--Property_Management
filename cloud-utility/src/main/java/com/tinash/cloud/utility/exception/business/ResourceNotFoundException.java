package com.tinash.cloud.utility.exception.business;

import com.tinash.cloud.utility.exception.base.BusinessException;

/**
 * Exception thrown when a requested resource is not found.
 */
public class ResourceNotFoundException extends BusinessException {
    
    public ResourceNotFoundException(String resourceType, Object id) {
        super(
            String.format("%s not found with id: %s", resourceType, id),
            "RESOURCE_NOT_FOUND"
        );
    }
    
    public ResourceNotFoundException(String message) {
        super(message, "RESOURCE_NOT_FOUND");
    }
    
    @Override
    public int getHttpStatus() {
        return 404;
    }
}
