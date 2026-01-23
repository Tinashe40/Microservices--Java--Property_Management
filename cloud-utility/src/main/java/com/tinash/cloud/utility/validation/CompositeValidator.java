package com.tinash.cloud.utility.validation;

import java.util.Arrays;
import java.util.List;

/**
 * Composite validator that combines multiple validators.
 */
public class CompositeValidator<T> implements Validator<T> {
    
    private final List<Validator<T>> validators;
    
    @SafeVarargs
    public CompositeValidator(Validator<T>... validators) {
        this.validators = Arrays.asList(validators);
    }
    
    @Override
    public ValidationResult validate(T object) {
        return validators.stream()
                .map(validator -> validator.validate(object))
                .reduce(ValidationResult.valid(), ValidationResult::merge);
    }
}
