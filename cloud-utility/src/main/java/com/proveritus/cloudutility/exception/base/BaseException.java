package com.proveritus.cloudutility.exception.base;

import java.time.Instant;
import java.util.UUID;

/**
 * Base exception for all custom exceptions in the system.
 * Provides common fields and behavior for exception handling.
 */
public abstract class BaseException extends RuntimeException {
    
    private final String errorId;
    private final String errorCode;
    private final Instant timestamp;
    
    protected BaseException(String message, String errorCode) {
        this(message, errorCode, null);
    }
    
    protected BaseException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorId = UUID.randomUUID().toString();
        this.errorCode = errorCode;
        this.timestamp = Instant.now();
    }
    
    public String getErrorId() {
        return errorId;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public Instant getTimestamp() {
        return timestamp;
    }
    
    public abstract int getHttpStatus();
}