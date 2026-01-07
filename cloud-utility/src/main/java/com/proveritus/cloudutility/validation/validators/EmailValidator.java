package com.proveritus.cloudutility.validation.validators;

import com.proveritus.cloudutility.validation.ValidationResult;
import com.proveritus.cloudutility.validation.Validator;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class EmailValidator implements Validator<String> {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"
    );

    @Override
    public ValidationResult validate(String email) {
        if (StringUtils.isBlank(email)) {
            return ValidationResult.invalid("email", "Email cannot be blank");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return ValidationResult.invalid("email", "Invalid email format");
        }
        return ValidationResult.valid();
    }
}
