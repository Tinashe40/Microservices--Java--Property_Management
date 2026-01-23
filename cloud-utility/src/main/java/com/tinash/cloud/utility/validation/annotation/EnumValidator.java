package com.tinash.cloud.utility.validation.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EnumValidator implements ConstraintValidator<ValidEnum, String> {

    private Set<String> allowedValues;

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        allowedValues = Stream.of(constraintAnnotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || allowedValues.contains(value);
    }
}
