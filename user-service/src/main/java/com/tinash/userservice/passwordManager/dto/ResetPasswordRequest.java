package com.tinash.userservice.passwordManager.dto;

import lombok.Data;

@Data
@PasswordMatches
public class ResetPasswordRequest implements PasswordConfirmation {
    private String newPassword;
    private String confirmPassword;

    @Override
    public String getNewPassword() {
        return newPassword;
    }

    @Override
    public String getConfirmPassword() {
        return confirmPassword;
    }
}
