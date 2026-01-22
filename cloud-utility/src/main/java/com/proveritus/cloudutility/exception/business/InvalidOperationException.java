package com.proveritus.cloudutility.exception.business;

import com.tinash.cloud.utility.exception.base.BusinessException;

/**
 * Exception thrown when an operation is not allowed.
 */
public class InvalidOperationException extends BusinessException {
    
    public InvalidOperationException(String message) {
        super(message, "INVALID_OPERATION");
    }
    
    @Override
    public int getHttpStatus() {
        return 400;
    }
}