package com.tinash.userservice.userManager.validation;


import com.tinash.cloud.utility.dto.common.UserDto;
import org.springframework.stereotype.Component;

@Component
public class UserValidator {

    public void validate(UserDto userDTO, boolean isUsernameTaken, boolean isEmailTaken) {
        if (isUsernameTaken) {
            throw new IllegalArgumentException("Username is already taken!");
        }

        if (isEmailTaken) {
            throw new IllegalArgumentException("Email Address already in use!");
        }
    }
}
