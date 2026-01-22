package com.proveritus.userservice.auth.dto.responses;

import com.tinash.cloud.utility.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {

    private JwtAuthResponse token;
    private UserDto user;
}
