package com.proveritus.apigateway.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

@Configuration
public class GatewayConfig {

    private static final Logger log = LoggerFactory.getLogger(GatewayConfig.class);

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("user-service",r -> r.path("/api/users/**", "/api/auth/**", "/api/roles/**", "/api/permissions/**")
                        .filters(f -> f.stripPrefix(1).filter((exchange, chain) -> {
                            log.info("Request to user-service: {} {}", exchange.getRequest().getMethod(), exchange.getRequest().getURI());
                            HttpHeaders headers = exchange.getRequest().getHeaders();
                            headers.forEach((key, value) -> log.info("Header: {}={}", key, value));
                            return chain.filter(exchange);
                        }))
                        .uri("lb://user-service"))
                .route("property-service", r -> r.path("/api/properties/**", "/api/floors/**", "/api/units/**")
                        .filters(f -> f.stripPrefix(1).filter((exchange, chain) -> {
                            log.info("Request to property-service: {} {}", exchange.getRequest().getMethod(), exchange.getRequest().getURI());
                            HttpHeaders headers = exchange.getRequest().getHeaders();
                            headers.forEach((key, value) -> log.info("Header: {}={}", key, value));
                            return chain.filter(exchange);
                        }))
                        .uri("lb://property-service"))
                .build();
    }
}
