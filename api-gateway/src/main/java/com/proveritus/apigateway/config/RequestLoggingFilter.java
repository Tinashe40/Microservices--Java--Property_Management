package com.proveritus.apigateway.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Gateway filter for logging incoming requests.
 * Follows Single Responsibility Principle.
 */
@Slf4j
@Component
public class RequestLoggingFilter implements GatewayFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logRequest(exchange);
        return chain.filter(exchange);
    }

    private void logRequest(ServerWebExchange exchange) {
        log.info("Request: {} {} from {}",
                exchange.getRequest().getMethod(),
                exchange.getRequest().getURI(),
                exchange.getRequest().getRemoteAddress());

        if (log.isDebugEnabled()) {
            exchange.getRequest().getHeaders()
                    .forEach((key, value) ->
                            log.debug("Header: {}={}", key, value));
        }
    }
}