package com.proveritus.userservice.userManager.userRoles.dto;

import com.proveritus.cloudutility.jpa.Updatable;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateRoleDTO implements Updatable {

    private Long id;

    @NotBlank
    private String name;

    @Override
    public Long getId() {
        return id;
    }
}
