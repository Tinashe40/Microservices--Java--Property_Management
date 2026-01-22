package com.proveritus.cloudutility.exception.domain;

import com.tinash.cloud.utility.exception.base.BusinessException;

/**
 * Base exception for domain-specific errors.
 */
public class DomainException extends BusinessException {
    
    protected DomainException(String message, String errorCode) {
        super(message, errorCode);
    }
    
    protected DomainException(String message, String errorCode, Throwable cause) {
        super(message, errorCode, cause);
    }
}