package com.proveritus.cloudutility.messaging.handler;

public interface EventHandler<T> {

    void handle(T event);
}