package com.tinash.cloud.utility.validation.annotation;

import com.tinash.cloud.utility.password.PasswordPolicyValidator;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidPasswordValidator implements ConstraintValidator<ValidPassword, String> {

    private final PasswordPolicyValidator passwordPolicyValidator;
    private String message;

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.isEmpty()) {
            return false; // Or true, depending on whether null/empty passwords are allowed by default
        }

        boolean valid = passwordPolicyValidator.isValid(password);

        if (!valid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(passwordPolicyValidator.getPolicyMessage())
                    .addConstraintViolation();
        }
        return valid;
    }
}
