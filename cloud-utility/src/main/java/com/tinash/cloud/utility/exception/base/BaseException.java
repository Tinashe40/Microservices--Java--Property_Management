package com.tinash.cloud.utility.exception.base;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

/**
 * Base exception for all custom exceptions in the system.
 * Provides common fields and behavior for exception handling.
 */
@Getter
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

    public abstract int getHttpStatus();
}
