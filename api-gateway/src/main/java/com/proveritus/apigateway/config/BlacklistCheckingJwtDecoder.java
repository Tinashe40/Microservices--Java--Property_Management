package com.proveritus.apigateway.config;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class BlacklistCheckingJwtDecoder implements ReactiveJwtDecoder {

    private final ReactiveJwtDecoder delegate;
    private final WebClient.Builder webClientBuilder;

    public BlacklistCheckingJwtDecoder(ReactiveJwtDecoder delegate, WebClient.Builder webClientBuilder) {
        this.delegate = delegate;
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public Mono<Jwt> decode(String token) {
        return delegate.decode(token)
                .flatMap(jwt -> webClientBuilder.build().post()
                        .uri("http://user-service/auth/is-blacklisted")
                        .bodyValue(token)
                        .retrieve()
                        .bodyToMono(Boolean.class)
                        .flatMap(isBlacklisted -> {
                            if (isBlacklisted) {
                                return Mono.error(new OAuth2AuthenticationException("Token is blacklisted"));
                            }
                            return Mono.just(jwt);
                        }));
    }
}
