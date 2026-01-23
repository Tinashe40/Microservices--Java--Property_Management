package com.tinash.cloud.utility.validation.validators;

import com.tinash.cloud.utility.validation.ValidationResult;
import com.tinash.cloud.utility.validation.Validator;

import java.util.Collection;
import java.util.Map;

public class SizeValidator implements Validator<Object> {

    private final String fieldName;
    private final int min;
    private final int max;

    public SizeValidator(String fieldName, int min, int max) {
        this.fieldName = fieldName;
        this.min = min;
        this.max = max;
    }

    @Override
    public ValidationResult validate(Object value) {
        if (value == null) {
            return ValidationResult.valid(); // or invalid, depending on requirements
        }

        int size;
        if (value instanceof CharSequence) {
            size = ((CharSequence) value).length();
        } else if (value instanceof Collection) {
            size = ((Collection<?>) value).size();
        } else if (value instanceof Map) {
            size = ((Map<?, ?>) value).size();
        } else if (value.getClass().isArray()) {
            size = java.lang.reflect.Array.getLength(value);
        } else {
            return ValidationResult.invalid(fieldName, "Unsupported type for size validation");
        }

        if (size < min || size > max) {
            return ValidationResult.invalid(fieldName, "size must be between " + min + " and " + max);
        }

        return ValidationResult.valid();
    }
}
