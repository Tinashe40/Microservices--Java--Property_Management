package com.tinash.cloud.utility.exception;

import lombok.Getter;

import java.time.Duration;

/**
 * Exception thrown when rate limit is exceeded.
 */
@Getter
public class TooManyRequestsException extends RuntimeException {

    private final Duration retryAfter;

    public TooManyRequestsException(String message, Duration retryAfter) {
        super(message);
        this.retryAfter = retryAfter;
    }

    public long getRetryAfterSeconds() {
        return retryAfter.getSeconds();
    }
}