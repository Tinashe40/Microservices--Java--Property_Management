package com.proveritus.userservice.userManager.dto;

import com.proveritus.cloudutility.jpa.Updatable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;

@Data
public class UpdateUserDTO implements Updatable {

    private Long id;

    @Email
    private String email;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private Set<String> roles;

    private boolean enabled;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;

    @Override
    public Long getId() {
        return id;
    }
}
