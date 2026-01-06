package com.proveritus.cloudutility.core.domain;

import java.time.Instant;
import java.util.UUID;

/**
 * Base class for domain events.
 * Follows DDD principles for event-driven architecture.
 */
public abstract class DomainEvent {
    
    private final String eventId;
    private final Instant occurredOn;
    private final String eventType;

    protected DomainEvent() {
        this.eventId = UUID.randomUUID().toString();
        this.occurredOn = Instant.now();
        this.eventType = this.getClass().getSimpleName();
    }

    public String getEventId() {
        return eventId;
    }

    public Instant getOccurredOn() {
        return occurredOn;
    }

    public String getEventType() {
        return eventType;
    }
}