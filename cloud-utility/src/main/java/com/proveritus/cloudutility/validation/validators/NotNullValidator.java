package com.proveritus.cloudutility.validation.validators;

import com.proveritus.cloudutility.validation.ValidationResult;
import com.proveritus.cloudutility.validation.Validator;

public class NotNullValidator implements Validator<Object> {

    private final String fieldName;

    public NotNullValidator(String fieldName) {
        this.fieldName = fieldName;
    }

    @Override
    public ValidationResult validate(Object object) {
        if (object == null) {
            return ValidationResult.invalid(fieldName, "must not be null");
        }
        return ValidationResult.valid();
    }
}
