package com.proveritus.userservice.userGroups.dto;

import com.proveritus.cloudutility.jpa.Updatable;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PermissionDTO implements Updatable {
    private Long id;

    @NotBlank(message = "Permission name cannot be blank")
    private String name;

    @Override
    public Long getId() {
        return id;
    }
}