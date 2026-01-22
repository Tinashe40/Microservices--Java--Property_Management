package com.proveritus.userservice.domain.model.user;

import com.tinash.cloud.utility.core.domain.DomainEvent;

public class UserCreatedEvent extends DomainEvent {
    private final Long userId;
    private final String username;
    private final String email;

    public UserCreatedEvent(Long userId, String username, String email) {
        super();
        this.userId = userId;
        this.username = username;
        this.email = email;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}
