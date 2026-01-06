package com.proveritus.userservice.domain.model.user;

import com.proveritus.cloudutility.core.domain.DomainEvent;

import java.time.Instant;

public class PasswordChangedEvent extends DomainEvent {
    private final Long userId;
    private final Instant changedAt;

    public PasswordChangedEvent(Long userId, Instant changedAt) {
        this.userId = userId;
        this.changedAt = changedAt;
    }

    public Long getUserId() {
        return userId;
    }

    public Instant getChangedAt() {
        return changedAt;
    }
}
