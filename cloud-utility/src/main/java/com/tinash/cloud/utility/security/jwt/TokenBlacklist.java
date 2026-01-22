package com.tinash.cloud.utility.security.jwt;

import org.springframework.context.annotation.Configuration;

@Configuration
public interface TokenBlacklist {
    boolean isTokenBlacklisted(String token);
}
