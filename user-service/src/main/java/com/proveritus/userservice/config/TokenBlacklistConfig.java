package com.proveritus.userservice.config;

import com.proveritus.cloudutility.security.TokenBlacklist;
import com.proveritus.userservice.jwt.TokenBlacklistService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TokenBlacklistConfig {

    @Bean
    public TokenBlacklist tokenBlacklist(TokenBlacklistService tokenBlacklistService) {
        return tokenBlacklistService;
    }
}
