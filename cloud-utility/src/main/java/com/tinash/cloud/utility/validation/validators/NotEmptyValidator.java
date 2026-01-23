package com.tinash.cloud.utility.validation.validators;

import com.tinash.cloud.utility.validation.ValidationResult;
import com.tinash.cloud.utility.validation.Validator;
import org.apache.commons.lang3.StringUtils;

public class NotEmptyValidator implements Validator<String> {

    private final String fieldName;

    public NotEmptyValidator(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public ValidationResult validate(String value) {
        if (StringUtils.isEmpty(value)) {
            return ValidationResult.invalid(fieldName, "must not be empty");
        }
        return ValidationResult.valid();
    }
}
