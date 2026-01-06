package com.proveritus.userservice.domain.model.user;

import com.proveritus.cloudutility.core.domain.DomainEvent;

import java.time.Instant;

public class UserGroupsAssignedEvent extends DomainEvent {
    private final Long userId;
    private final int numberOfGroups;

    public UserGroupsAssignedEvent(Long userId, int numberOfGroups) {
        this.userId = userId;
        this.numberOfGroups = numberOfGroups;
    }

    public Long getUserId() {
        return userId;
    }

    public int getNumberOfGroups() {
        return numberOfGroups;
    }
}

class AccountLockedEvent extends DomainEvent {
    private final Long userId;
    private final Instant lockedAt;

    public AccountLockedEvent(Long userId, Instant lockedAt) {
        this.userId = userId;
        this.lockedAt = lockedAt;
    }

    public Long getUserId() {
        return userId;
    }

    public Instant getLockedAt() {
        return lockedAt;
    }
}

class AccountUnlockedEvent extends DomainEvent {
    private final Long userId;
    private final Instant unlockedAt;

    public AccountUnlockedEvent(Long userId, Instant unlockedAt) {
        this.userId = userId;
        this.unlockedAt = unlockedAt;
    }

    public Long getUserId() {
        return userId;
    }

    public Instant getUnlockedAt() {
        return unlockedAt;
    }
}

class UserDeactivatedEvent extends DomainEvent {
    private final Long userId;
    private final Instant deactivatedAt;

    public UserDeactivatedEvent(Long userId, Instant deactivatedAt) {
        this.userId = userId;
        this.deactivatedAt = deactivatedAt;
    }

    public Long getUserId() {
        return userId;
    }

    public Instant getDeactivatedAt() {
        return deactivatedAt;
    }
}

class UserActivatedEvent extends DomainEvent {
    private final Long userId;
    private final Instant activatedAt;

    public UserActivatedEvent(Long userId, Instant activatedAt) {
        this.userId = userId;
        this.activatedAt = activatedAt;
    }

    public Long getUserId() {
        return userId;
    }

    public Instant getActivatedAt() {
        return activatedAt;
    }
}
