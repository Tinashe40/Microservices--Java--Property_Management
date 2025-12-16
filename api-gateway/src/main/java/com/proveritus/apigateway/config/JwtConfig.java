package com.proveritus.apigateway.config;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.web.reactive.function.client.WebClient;

import javax.crypto.SecretKey;
import java.time.Duration;

/**
 * JWT configuration for reactive JWT decoding.
 * Configures the JWT decoder with blacklist checking capability.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class JwtConfig {

    private final SecurityProperties securityProperties;
    private final WebClient.Builder webClientBuilder;

    /**
     * Creates a reactive JWT decoder with blacklist checking.
     *
     * @return configured reactive JWT decoder
     */
    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        NimbusReactiveJwtDecoder baseDecoder = createBaseDecoder();
        return new BlacklistCheckingJwtDecoder(baseDecoder, webClientBuilder, securityProperties.getJwt());
    }

    /**
     * Creates the base Nimbus JWT decoder with HMAC SHA-512 algorithm.
     */
    private NimbusReactiveJwtDecoder createBaseDecoder() {
        SecretKey key = decodeSecretKey();
        NimbusReactiveJwtDecoder decoder = NimbusReactiveJwtDecoder.withSecretKey(key)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();

        // Add a clock skew to tolerate minor time differences
        decoder.setJwtValidator(
                new DelegatingOAuth2TokenValidator<>(
                        JwtValidators.createDefaultWithIssuer(securityProperties.getJwt().getIssuer()),
                        new JwtTimestampValidator(Duration.ofSeconds(30))
                )
        );

        return decoder;
    }

    /**
     * Decodes the Base64-encoded JWT secret into a SecretKey.
     */
    private SecretKey decodeSecretKey() {
        try {
            String secret = securityProperties.getJwt().getSecret();
            if (secret == null || secret.isBlank()) {
                throw new IllegalStateException("JWT secret is not configured. Please set the 'security.jwt.secret' property.");
            }
            byte[] keyBytes = Decoders.BASE64.decode(secret);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (IllegalArgumentException e) {
            log.error("Failed to decode JWT secret key. It must be Base64 encoded.", e);
            throw new IllegalStateException("Invalid JWT secret configuration. Check 'security.jwt.secret' property.", e);
        }
    }
}