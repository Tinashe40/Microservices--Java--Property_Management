package com.proveritus.cloudutility.exception.business;

import com.proveritus.cloudutility.exception.base.BusinessException;

/**
 * Exception thrown when user lacks required permissions.
 */
public class ForbiddenException extends BusinessException {
    
    public ForbiddenException(String message) {
        super(message, "FORBIDDEN");
    }
    
    @Override
    public int getHttpStatus() {
        return 403;
    }
}