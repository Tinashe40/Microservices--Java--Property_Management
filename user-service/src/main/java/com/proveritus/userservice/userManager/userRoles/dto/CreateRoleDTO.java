package com.proveritus.userservice.userManager.userRoles.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateRoleDTO {
    @NotBlank
    private String name;
}
