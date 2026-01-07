package com.proveritus.cloudutility.security.jwt;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class JwtTokenProviderTest {

    @Test
    void testJwtTokenProvider() {
        JwtProperties jwtProperties = new JwtProperties();
        jwtProperties.setSecret("secret");
        assertNotNull(new JwtTokenProvider(jwtProperties));
    }
}
