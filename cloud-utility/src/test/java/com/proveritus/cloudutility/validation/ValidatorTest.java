package com.proveritus.cloudutility.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ValidatorTest {

    @Test
    void testValidator() {
        assertNotNull((Validator<String>) s -> ValidationResult.valid());
    }
}
