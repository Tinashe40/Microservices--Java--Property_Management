package com.proveritus.apigateway.config;

import com.proveritus.apigateway.config.SecurityProperties.Jwt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * JWT decoder that checks token blacklist status before accepting tokens.
 * Implements Decorator pattern to extend base decoder functionality.
 */
@Slf4j
public class BlacklistCheckingJwtDecoder implements ReactiveJwtDecoder {

    private final ReactiveJwtDecoder delegate;
    private final WebClient webClient;
    private final SecurityProperties.Jwt jwtProperties;

    public BlacklistCheckingJwtDecoder(ReactiveJwtDecoder delegate, WebClient.Builder webClientBuilder, SecurityProperties.Jwt jwtProperties) {
        this.delegate = delegate;
        this.webClient = webClientBuilder.build();
        this.jwtProperties = jwtProperties;
    }

    /**
     * Decodes and validates JWT token, including blacklist verification.
     *
     * @param token the JWT token string
     * @return decoded JWT wrapped in Mono
     * @throws OAuth2AuthenticationException if token is blacklisted or invalid
     */
    @Override
    public Mono<org.springframework.security.oauth2.jwt.Jwt> decode(String token) {
        return delegate.decode(token)
                .flatMap(jwt -> verifyTokenNotBlacklisted(token, jwt))
                .doOnError(error -> log.error("JWT validation failed for token: {}",
                        maskToken(token), error));
    }

    /**
     * Verifies that the token is not blacklisted.
     */
    private Mono<org.springframework.security.oauth2.jwt.Jwt> verifyTokenNotBlacklisted(String token, org.springframework.security.oauth2.jwt.Jwt jwt) {
        return webClient
                .post()
                .uri(jwtProperties.getBlacklistCheckUrl())
                .bodyValue(token)
                .retrieve()
                .bodyToMono(Boolean.class)
                .flatMap(isBlacklisted ->
                        handleBlacklistResponse(isBlacklisted, jwt))
                .onErrorResume(error -> {
                    log.error("Blacklist check failed, rejecting token.", error);
                    return Mono.error(new OAuth2AuthenticationException(
                            new OAuth2Error("server_error", "Failed to verify token blacklist status.", null), error));
                });
    }

    /**
     * Handles the blacklist check response.
     */
    private Mono<org.springframework.security.oauth2.jwt.Jwt> handleBlacklistResponse(Boolean isBlacklisted, org.springframework.security.oauth2.jwt.Jwt jwt) {
        if (Boolean.TRUE.equals(isBlacklisted)) {
            log.warn("Blocked blacklisted token for subject: {}", jwt.getSubject());
            return Mono.error(new OAuth2AuthenticationException(
                    new OAuth2Error("invalid_token", "Token has been revoked", null)));
        }
        return Mono.just(jwt);
    }

    /**
     * Masks the JWT token for secure logging, showing only the first and last 10 characters.
     * This prevents accidental exposure of the full token in logs while still allowing for some identification.
     */
    private String maskToken(String token) {
        if (token == null || token.length() < 20) {
            return "***";
        }
        return token.substring(0, 10) + "..." + token.substring(token.length() - 10);
    }
}
