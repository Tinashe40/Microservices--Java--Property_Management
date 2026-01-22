package com.proveritus.cloudutility.exception.business;

import com.tinash.cloud.utility.exception.base.BusinessException;

/**
 * Exception thrown for conflicting operations.
 */
public class ConflictException extends BusinessException {
    
    public ConflictException(String message) {
        super(message, "CONFLICT");

    }
    
    @Override
    public int getHttpStatus() {
        return 409;
    }
}