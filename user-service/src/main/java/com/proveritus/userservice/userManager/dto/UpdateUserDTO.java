package com.proveritus.userservice.userManager.dto;

import com.proveritus.cloudutility.jpa.Updatable;
import lombok.Data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDTO implements Updatable {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private boolean enabled;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    public Long getId() {
        return id;
    }
}