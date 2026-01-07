package com.proveritus.cloudutility.messaging.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DomainEventListener {

    @EventListener
    public void handleEvent(Object event) {
        // Handle domain event
    }
}