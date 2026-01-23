package com.tinash.cloud.utility.client;

import feign.Logger;
import feign.RequestInterceptor;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Global Feign configuration.
 * This class provides common beans for all Feign clients to ensure consistent behavior
 * across microservices communication.
 */
@Configuration
public class FeignConfig {

    /**
     * Provides a custom ErrorDecoder to handle specific HTTP error codes
     * and translate them into application-specific exceptions.
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder();
    }

    /**
     * Configures a Feign Logger to log requests and responses.
     * LogLevel.FULL is often good for development, but consider
     * LogLevel.BASIC or LogLevel.HEADERS for production.
     */
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    /**
     * Configures a Feign Retryer.
     * This will retry failed requests with an exponential backoff.
     * - period: How long to wait before first retry (100ms)
     * - maxPeriod: Maximum wait time between retries (1 second)
     * - maxAttempts: Maximum number of retries (5 attempts)
     */
    @Bean
    public Retryer retryer() {
        return new Retryer.Default(100, 1000, 5);
    }

    /**
     * Example RequestInterceptor to add common headers (e.g., Authorization).
     * This can be used to propagate security context (JWT token) across service calls.
     * Uncomment and implement if cross-service authentication is needed.
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest httpRequest = attributes.getRequest();
                String authorization = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);
                if (authorization != null) {
                    requestTemplate.header(HttpHeaders.AUTHORIZATION, authorization);
                }
            }
        };
    }

}
