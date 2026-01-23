package com.tinash.cloud.utility.exception.business;

import com.tinash.cloud.utility.exception.base.BusinessException;

/**
 * Exception thrown for bad request data.
 */
public class BadRequestException extends BusinessException {
    
    public BadRequestException(String message) {
        super(message, "BAD_REQUEST");
    }
    
    @Override
    public int getHttpStatus() {
        return 400;
    }
}
