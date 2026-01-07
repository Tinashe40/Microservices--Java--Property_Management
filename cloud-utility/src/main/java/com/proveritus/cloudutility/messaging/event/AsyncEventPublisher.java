package com.proveritus.cloudutility.messaging.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AsyncEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public AsyncEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Async
    public void publish(Object event) {
        applicationEventPublisher.publishEvent(event);
    }
}