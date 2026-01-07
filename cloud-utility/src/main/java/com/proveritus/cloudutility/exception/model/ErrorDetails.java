package com.proveritus.cloudutility.exception.model;

import lombok.Getter;

import java.time.Instant;

@Getter
public class ErrorDetails {

    private final Instant timestamp;
    private final String message;
    private final String details;

    public ErrorDetails(Instant timestamp, String message, String details) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }
}
