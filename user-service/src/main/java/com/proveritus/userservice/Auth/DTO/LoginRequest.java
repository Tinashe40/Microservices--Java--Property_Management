package com.proveritus.userservice.Auth.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotBlank
    private String usernameOrEmail;

    @NotBlank
    private String password;
}
