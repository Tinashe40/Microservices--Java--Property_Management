package com.proveritus.userservice.userManager.validation;

import com.proveritus.userservice.userManager.dto.ResetPasswordRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context){
        try {
            final Object passwordObj = obj.getClass().getMethod("getNewPassword").invoke(obj);
            final Object confirmPasswordObj = obj.getClass().getMethod("getConfirmPassword").invoke(obj);
            return passwordObj != null && passwordObj.equals(confirmPasswordObj);
        } catch (final Exception ex) {
            return false;
        }
    }
}
