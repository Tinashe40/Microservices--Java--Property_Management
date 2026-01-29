package com.tinash.propertyservice.application.port.out;

import com.tinash.cloud.utility.core.domain.DomainEvent;

/**
 * Port for publishing domain events.
 * Follows Dependency Inversion Principle.
 */
public interface EventPublisherPort {
    void publish(DomainEvent event);
}
