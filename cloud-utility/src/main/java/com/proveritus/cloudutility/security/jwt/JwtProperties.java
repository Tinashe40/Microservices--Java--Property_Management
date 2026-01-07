package com.proveritus.cloudutility.security.jwt;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
@ConfigurationProperties(prefix = "app.security.jwt")
public class JwtProperties {

    @NotBlank(message = "JWT secret must not be blank")
    private String secret;

    @Positive(message = "JWT expiration must be positive")
    private long expiration = 86400000; // 24 hours

    @Positive(message = "Refresh token expiration must be positive")
    private long refreshExpiration = 604800000; // 7 days

    private String issuer = "CloudUtility";
}