package com.proveritus.userservice.userGroups.dto;

import com.tinash.cloud.utility.jpa.Updatable;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UserGroupDTO implements Updatable {
    private Long id;

    @NotBlank(message = "UserGroup name cannot be blank")
    private String name;

    private Set<String> permissions;

    @Override
    public Long getId() {
        return id;
    }
}