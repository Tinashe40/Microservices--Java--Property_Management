package com.proveritus.cloudutility.core.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Base exception for business logic violations (4xx errors).
 */
@Getter
@Setter
public class BusinessException extends RuntimeException {

    private final String errorCode;

    public BusinessException(String message) {
        this(message, null, null);
    }

    public BusinessException(String message, String errorCode) {
        this(message, errorCode, null);
    }

    public BusinessException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
