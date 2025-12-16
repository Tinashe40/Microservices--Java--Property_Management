package com.proveritus.apigateway.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    private final RequestLoggingFilter requestLoggingFilter;

    @Bean
    public GlobalFilter customGlobalFilter() {
        return (exchange, chain) -> requestLoggingFilter.filter(exchange, chain);
    }
}