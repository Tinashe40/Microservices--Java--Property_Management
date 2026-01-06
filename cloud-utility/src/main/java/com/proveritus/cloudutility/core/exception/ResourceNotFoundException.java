package com.proveritus.cloudutility.core.exception;

/**
 * Resource not found exception.
 */
class ResourceNotFoundException extends BusinessException {

    public ResourceNotFoundException(String resourceType, Object id) {
        super(String.format("%s not found with id: %s", resourceType, id), "RESOURCE_NOT_FOUND");
    }
}
