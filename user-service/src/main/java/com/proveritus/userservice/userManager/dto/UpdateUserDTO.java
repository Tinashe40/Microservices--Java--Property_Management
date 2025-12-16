package com.proveritus.userservice.userManager.dto;

import com.proveritus.cloudutility.jpa.Updatable;
import lombok.Data;

@Data
public class UpdateUserDTO implements Updatable {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
}
