package com.proveritus.cloudutility.messaging.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ApplicationEventListener implements ApplicationListener<org.springframework.context.ApplicationEvent> {

    @Override
    public void onApplicationEvent(org.springframework.context.ApplicationEvent event) {
        // Handle application event
    }
}