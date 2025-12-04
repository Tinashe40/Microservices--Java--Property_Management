package com.proveritus.cloudutility.security;

import org.springframework.context.annotation.Configuration;

@Configuration
public interface TokenBlacklist {
    boolean isTokenBlacklisted(String token);
}
