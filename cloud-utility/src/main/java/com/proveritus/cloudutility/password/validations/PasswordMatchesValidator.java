package com.proveritus.cloudutility.password.validations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, PasswordConfirmation> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(PasswordConfirmation value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        String newPassword = value.getNewPassword();
        String confirmPassword = value.getConfirmPassword();
        return newPassword != null && newPassword.equals(confirmPassword);
    }
}
