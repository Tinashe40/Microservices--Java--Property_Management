package com.tinash.cloud.utility.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Validator for the {@link ValidEnum} annotation.
 * Checks if a given string value matches one of the names of the specified enum class.
 * Can be configured to ignore case during validation.
 */
public class ValidEnumValidator implements ConstraintValidator<ValidEnum, String> {

    private Set<String> acceptedValues;
    private boolean ignoreCase;

    @Override
    public void initialize(ValidEnum annotation) {
        acceptedValues = Arrays.stream(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toSet());
        ignoreCase = annotation.ignoreCase();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // Null values should be handled by @NotNull or @NotEmpty
        }
        if (ignoreCase) {
            return acceptedValues.stream().anyMatch(e -> e.equalsIgnoreCase(value));
        } else {
            return acceptedValues.contains(value);
        }
    }
}
