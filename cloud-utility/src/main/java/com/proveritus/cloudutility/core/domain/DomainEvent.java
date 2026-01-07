package com.proveritus.cloudutility.core.domain;

import java.time.Instant;

public interface DomainEvent {

    Instant occurredOn();
}