package com.proveritus.cloudutility.exception.domain;

/**
 * Exception thrown when an aggregate root is not found.
 */
public class AggregateNotFoundException extends DomainException {
    
    public AggregateNotFoundException(String aggregateType, Object id) {
        super(
            String.format("%s aggregate not found with id: %s", aggregateType, id),
            "AGGREGATE_NOT_FOUND"
        );
    }
    
    @Override
    public int getHttpStatus() {
        return 404;
    }
}