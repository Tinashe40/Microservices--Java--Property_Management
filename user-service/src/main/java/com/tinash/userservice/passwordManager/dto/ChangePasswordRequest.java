package com.tinash.userservice.passwordManager.dto;

import com.tinash.cloud.utility.password.validations.PasswordConfirmation;
import com.tinash.cloud.utility.password.validations.PasswordMatches;
import lombok.Data;

@Data
@PasswordMatches
public class ChangePasswordRequest implements PasswordConfirmation {

    private String oldPassword;
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
