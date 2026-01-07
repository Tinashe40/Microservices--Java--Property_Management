package com.proveritus.cloudutility.exception.technical;

import com.proveritus.cloudutility.exception.base.TechnicalException;

/**
 * Exception thrown for database-related errors.
 */
public class DatabaseException extends TechnicalException {
    
    public DatabaseException(String message, Throwable cause) {
        super(message, "DATABASE_ERROR", cause);
    }
    
    @Override
    public int getHttpStatus() {
        return 500;
    }
}