package com.tinash.userservice.domain.event;

import com.tinash.cloud.utility.domain.DomainEvent;

import java.time.Instant;

public class UserGroupsAssignedEvent extends DomainEvent {
    private final String userId;
    private final int numberOfGroups;

    public UserGroupsAssignedEvent(String userId, int numberOfGroups) {
        this.userId = userId;
        this.numberOfGroups = numberOfGroups;
    }

    public String getUserId() {
        return userId;
    }

    public int getNumberOfGroups() {
        return numberOfGroups;
    }
}

class AccountLockedEvent extends DomainEvent {
    private final String userId;
    private final Instant lockedAt;

    public AccountLockedEvent(String userId, Instant lockedAt) {
        this.userId = userId;
        this.lockedAt = lockedAt;
    }

    public String getUserId() {
        return userId;
    }

    public Instant getLockedAt() {
        return lockedAt;
    }
}

class AccountUnlockedEvent extends DomainEvent {
    private final String userId;
    private final Instant unlockedAt;

    public AccountUnlockedEvent(String userId, Instant unlockedAt) {
        this.userId = userId;
        this.unlockedAt = unlockedAt;
    }

    public String getUserId() {
        return userId;
    }

    public Instant getUnlockedAt() {
        return unlockedAt;
    }
}

class UserDeactivatedEvent extends DomainEvent {
    private final String userId;
    private final Instant deactivatedAt;

    public UserDeactivatedEvent(String userId, Instant deactivatedAt) {
        this.userId = userId;
        this.deactivatedAt = deactivatedAt;
    }

    public String getUserId() {
        return userId;
    }

    public Instant getDeactivatedAt() {
        return deactivatedAt;
    }
}

class UserActivatedEvent extends DomainEvent {
    private final String userId;
    private final Instant activatedAt;

    public UserActivatedEvent(String userId, Instant activatedAt) {
        this.userId = userId;
        this.activatedAt = activatedAt;
    }

    public String getUserId() {
        return userId;
    }

    public Instant getActivatedAt() {
        return activatedAt;
    }
}
