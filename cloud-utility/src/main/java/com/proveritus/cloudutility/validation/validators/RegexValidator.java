package com.proveritus.cloudutility.validation.validators;

import com.proveritus.cloudutility.validation.ValidationResult;
import com.proveritus.cloudutility.validation.Validator;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

public class RegexValidator implements Validator<String> {

    private final String fieldName;
    private final Pattern pattern;

    public RegexValidator(String fieldName, String regex) {
        this.fieldName = fieldName;
        this.pattern = Pattern.compile(regex);
    }

    @Override
    public ValidationResult validate(String value) {
        if (StringUtils.isEmpty(value)) {
            return ValidationResult.valid();
        }
        if (!pattern.matcher(value).matches()) {
            return ValidationResult.invalid(fieldName, "does not match pattern: " + pattern.pattern());
        }
        return ValidationResult.valid();
    }
}
