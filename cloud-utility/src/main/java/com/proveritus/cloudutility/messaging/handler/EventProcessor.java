package com.proveritus.cloudutility.messaging.handler;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventProcessor {

    private final List<EventHandler<?>> eventHandlers;

    public EventProcessor(List<EventHandler<?>> eventHandlers) {
        this.eventHandlers = eventHandlers;
    }

    public void process(Object event) {
        for (EventHandler eventHandler : eventHandlers) {
            // This is a simplified example. You would need to check if the handler supports the event type.
            eventHandler.handle(event);
        }
    }
}