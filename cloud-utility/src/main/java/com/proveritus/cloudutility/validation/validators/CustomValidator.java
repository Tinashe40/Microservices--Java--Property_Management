package com.proveritus.cloudutility.validation.validators;

import com.tinash.cloud.utility.validation.ValidationResult;
import com.tinash.cloud.utility.validation.Validator;

import java.util.function.Predicate;

public class CustomValidator<T> implements Validator<T> {

    private final String fieldName;
    private final String message;
    private final Predicate<T> predicate;

    public CustomValidator(String fieldName, String message, Predicate<T> predicate) {
        this.fieldName = fieldName;
        this.message = message;
        this.predicate = predicate;
    }

    @Override
    public ValidationResult validate(T value) {
        if (!predicate.test(value)) {
            return ValidationResult.invalid(fieldName, message);
        }
        return ValidationResult.valid();
    }
}
