package com.proveritus.apigateway.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Externalized security configuration properties.
 * Follows Single Responsibility Principle by managing only security-related properties.
 */
@Data
@Component
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {

    private List<String> allowedOrigins = List.of("http://localhost:3000");
    private List<String> allowedMethods = List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH");
    private List<String> allowedHeaders = List.of("*");
    private boolean allowCredentials = true;
    private long maxAge = 3600L;
    private String[] publicPaths = {"/api/auth/**", "/actuator/health"};
    private Jwt jwt = new Jwt();

    @Data
    public static class Jwt {
        private String secret;
        private String blacklistCheckUrl = "http://user-service/auth/is-blacklisted";
        private String issuer;
    }
}