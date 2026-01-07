package com.proveritus.cloudutility.exception.base;

/**
 * Base class for business logic exceptions (4xx HTTP status codes).
 * Used for validation errors, business rule violations, etc.
 */
public abstract class BusinessException extends BaseException {
    
    protected BusinessException(String message, String errorCode) {
        super(message, errorCode);
    }
    
    protected BusinessException(String message, String errorCode, Throwable cause) {
        super(message, errorCode, cause);
    }
    
    @Override
    public int getHttpStatus() {
        return 400; // Default to Bad Request
    }
}