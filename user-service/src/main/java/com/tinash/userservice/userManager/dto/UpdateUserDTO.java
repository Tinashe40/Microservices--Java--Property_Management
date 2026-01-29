package com.tinash.userservice.userManager.dto;

import lombok.Data;

@Data
public class UpdateUserDTO {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
}
