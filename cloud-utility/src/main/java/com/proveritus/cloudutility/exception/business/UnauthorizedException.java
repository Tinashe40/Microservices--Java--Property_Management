package com.proveritus.cloudutility.exception.business;

import com.proveritus.cloudutility.exception.base.BusinessException;

/**
 * Exception thrown when user is not authenticated.
 */
public class UnauthorizedException extends BusinessException {
    
    public UnauthorizedException(String message) {
        super(message, "UNAUTHORIZED");
    }
    
    @Override
    public int getHttpStatus() {
        return 401;
    }
}