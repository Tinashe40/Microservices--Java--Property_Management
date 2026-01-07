package com.proveritus.cloudutility.validation.validators;

import com.proveritus.cloudutility.validation.ValidationResult;
import com.proveritus.cloudutility.validation.Validator;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class PhoneValidator implements Validator<String> {

    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^\\+?[0-9. ()-]{7,25}$"
    );

    @Override
    public ValidationResult validate(String phone) {
        if (StringUtils.isBlank(phone)) {
            return ValidationResult.invalid("phone", "Phone number cannot be blank");
        }
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            return ValidationResult.invalid("phone", "Invalid phone number format");
        }
        return ValidationResult.valid();
    }
}
