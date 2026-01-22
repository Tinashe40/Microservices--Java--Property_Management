package com.proveritus.cloudutility.exception.technical;

import com.tinash.cloud.utility.exception.base.TechnicalException;

/**
 * Exception thrown when an operation times out.
 */
public class TimeoutException extends TechnicalException {
    
    private final long timeoutMillis;
    
    public TimeoutException(String operation, long timeoutMillis) {
        super(
            String.format("Operation '%s' timed out after %d ms", operation, timeoutMillis),
            "TIMEOUT_ERROR"
        );
        this.timeoutMillis = timeoutMillis;
    }
    
    public long getTimeoutMillis() {
        return timeoutMillis;
    }
    
    @Override
    public int getHttpStatus() {
        return 504; // Gateway Timeout
    }
}