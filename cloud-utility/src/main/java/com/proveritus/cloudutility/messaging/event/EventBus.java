package com.proveritus.cloudutility.messaging.event;

import com.google.common.eventbus.EventBus;
import org.springframework.stereotype.Component;

@Component
public class EventBus {

    private final com.google.common.eventbus.EventBus eventBus = new com.google.common.eventbus.EventBus();

    public void post(Object event) {
        eventBus.post(event);
    }

    public void register(Object listener) {
        eventBus.register(listener);
    }

    public void unregister(Object listener) {
        eventBus.unregister(listener);
    }
}