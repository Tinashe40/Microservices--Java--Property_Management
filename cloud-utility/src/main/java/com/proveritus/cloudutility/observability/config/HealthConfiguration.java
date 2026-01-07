package com.proveritus.cloudutility.observability.config;

import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HealthConfiguration {

    @Bean
    public HealthIndicator customHealthIndicator() {
        return () -> org.springframework.boot.actuate.health.Health.up().withDetail("app", "Alive and Kicking").build();
    }
}