package com.proveritus.propertyservice.application.port.out;

import com.proveritus.cloudutility.core.domain.DomainEvent;

/**
 * Port for publishing domain events.
 * Follows Dependency Inversion Principle.
 */
public interface EventPublisherPort {
    void publish(DomainEvent event);
}
