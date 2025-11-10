package com.proveritus.userservice.auth.dto.responses;

import com.proveritus.cloudutility.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {

    private JwtAuthResponse token;
    private UserDTO user;
}
