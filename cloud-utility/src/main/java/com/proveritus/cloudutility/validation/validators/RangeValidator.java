package com.proveritus.cloudutility.validation.validators;

import com.tinash.cloud.utility.validation.ValidationResult;
import com.tinash.cloud.utility.validation.Validator;

public class RangeValidator implements Validator<Number> {

    private final String fieldName;
    private final long min;
    private final long max;

    public RangeValidator(String fieldName, long min, long max) {
        this.fieldName = fieldName;
        this.min = min;
        this.max = max;
    }

    @Override
    public ValidationResult validate(Number value) {
        if (value == null) {
            return ValidationResult.valid(); // or invalid, depending on requirements
        }
        long longValue = value.longValue();
        if (longValue < min || longValue > max) {
            return ValidationResult.invalid(fieldName, "must be between " + min + " and " + max);
        }
        return ValidationResult.valid();
    }
}
