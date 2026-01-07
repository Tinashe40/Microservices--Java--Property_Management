package com.proveritus.cloudutility.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class GlobalExceptionHandlerTest {

    @Test
    void testGlobalExceptionHandler() {
        assertNotNull(new com.proveritus.cloudutility.exception.handler.GlobalExceptionHandler());
    }
}
