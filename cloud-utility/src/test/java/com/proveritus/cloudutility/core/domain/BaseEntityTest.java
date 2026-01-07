package com.proveritus.cloudutility.core.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class BaseEntityTest {

    @Test
    void testBaseEntity() {
        assertNotNull(new BaseEntity<>() {});
    }
}
