package com.proveritus.cloudutility.core.port.out;

import com.proveritus.cloudutility.core.domain.DomainEvent;

public interface EventPublisherPort {

    void publish(DomainEvent event);
}