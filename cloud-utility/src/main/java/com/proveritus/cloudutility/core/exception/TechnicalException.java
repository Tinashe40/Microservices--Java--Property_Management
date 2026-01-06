package com.proveritus.cloudutility.core.exception;

/**
 * Technical exception for system failures (5xx errors).
 */
class TechnicalException extends RuntimeException {

    private final String errorCode;

    public TechnicalException(String message, Throwable cause) {
        this(message, "TECHNICAL_ERROR", cause);
    }

    public TechnicalException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
