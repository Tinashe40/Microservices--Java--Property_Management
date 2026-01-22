package com.tinash.cloud.utility.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Custom validation annotation to check if a string value is one of the valid
 * names of a given enum class.
 * <p>
 * Example usage:
 * <pre>
 * {@code
 * @ValidEnum(enumClass = RoleType.class, message = "Invalid role type")
 * private String role;
 * }
 * </pre>
 */
@Documented
@Constraint(validatedBy = ValidEnumValidator.class)
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
public @interface ValidEnum {

    String message() default "Value is not valid.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<? extends Enum<?>> enumClass();

    boolean ignoreCase() default false; // Whether to ignore case when validating
}
