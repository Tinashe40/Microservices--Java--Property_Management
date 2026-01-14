package com.proveritus.cloudutility.client.resilience;

import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimiterConfiguration {

    @Bean
    public io.github.resilience4j.ratelimiter.RateLimiterConfig rateLimiterConfig() {
        return io.github.resilience4j.ratelimiter.RateLimiterConfig.custom()
                .limitRefreshPeriod(Duration.ofMillis(1000))
                .limitForPeriod(10)
                .timeoutDuration(Duration.ofMillis(25))
                .build();
    }
}