package com.proveritus.cloudutility.passwordManager.dto;

import com.proveritus.cloudutility.passwordManager.validations.PasswordConfirmation;
import com.proveritus.cloudutility.passwordManager.validations.PasswordMatches;
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
