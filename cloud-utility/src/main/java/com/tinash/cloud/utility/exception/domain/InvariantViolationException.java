package com.tinash.cloud.utility.exception.domain;

/**
 * Exception thrown when a domain invariant is violated.
 */
public class InvariantViolationException extends DomainException {
    
    public InvariantViolationException(String message) {
        super(message, "INVARIANT_VIOLATION");
    }
}
