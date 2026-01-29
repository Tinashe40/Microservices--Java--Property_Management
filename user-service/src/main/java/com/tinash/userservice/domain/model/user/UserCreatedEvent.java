package com.tinash.userservice.domain.model.user;

import com.tinash.cloud.utility.core.domain.DomainEvent;
import lombok.Getter;

@Getter
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

}
