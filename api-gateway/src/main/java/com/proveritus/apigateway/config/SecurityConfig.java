package com.proveritus.apigateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ReactiveJwtDecoder jwtDecoder;
    private final SecurityProperties securityProperties;

    /**
     * Configures the security filter chain for reactive web applications.
     *
     * @param http the server HTTP security configuration
     * @return configured security web filter chain
     */
    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeExchange(this::configureAuthorization)
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtDecoder(jwtDecoder)))
                .build();
    }

    /**
     * Configures authorization rules for different endpoints.
     */
    private void configureAuthorization(
            ServerHttpSecurity.AuthorizeExchangeSpec exchange) {
        exchange
                .pathMatchers(securityProperties.getPublicPaths()).permitAll()
                .anyExchange().authenticated();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final var source = new UrlBasedCorsConfigurationSource();
        final var config = new CorsConfiguration();

        config.setAllowedOrigins(securityProperties.getAllowedOrigins());
        config.setAllowedMethods(securityProperties.getAllowedMethods());
        config.setAllowedHeaders(securityProperties.getAllowedHeaders());
        config.setAllowCredentials(securityProperties.isAllowCredentials());
        config.setMaxAge(securityProperties.getMaxAge());

        source.registerCorsConfiguration("/**", config);
        return source;
    }
}