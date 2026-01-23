package com.tinash.cloud.utility.exception.base;

/**
 * Base class for technical exceptions (5xx HTTP status codes).
 * Used for system failures, infrastructure issues, etc.
 */
public abstract class TechnicalException extends BaseException {
    
    protected TechnicalException(String message, String errorCode) {
        super(message, errorCode);
    }
    
    protected TechnicalException(String message, String errorCode, Throwable cause) {
        super(message, errorCode, cause);
    }
    
    @Override
    public int getHttpStatus() {
        return 500;
    }
}
