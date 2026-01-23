package com.tinash.cloud.utility.validation.validators;

import com.tinash.cloud.utility.validation.annotation.ValidEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
