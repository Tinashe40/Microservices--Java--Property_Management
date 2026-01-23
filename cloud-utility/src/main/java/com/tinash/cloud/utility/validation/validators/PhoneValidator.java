package com.tinash.cloud.utility.validation.validators;

import com.tinash.cloud.utility.validation.annotation.ValidPhone;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {

    private static final Pattern PHONE_PATTERN = Pattern.compile(
            "^\\+?[0-9. ()-]{7,25}$"
    );

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(phone)) {
            return false;
        }
        return PHONE_PATTERN.matcher(phone).matches();
    }
}
