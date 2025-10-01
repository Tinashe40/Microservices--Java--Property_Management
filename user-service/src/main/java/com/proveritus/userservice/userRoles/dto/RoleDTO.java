package com.proveritus.userservice.userRoles.dto;

import com.proveritus.cloudutility.jpa.Updatable;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class RoleDTO implements Updatable {
    private Long id;

    @NotBlank(message = "Role name cannot be blank")
    private String name;

    private Set<String> permissions;

    @Override
    public Long getId() {
        return id;
    }
}