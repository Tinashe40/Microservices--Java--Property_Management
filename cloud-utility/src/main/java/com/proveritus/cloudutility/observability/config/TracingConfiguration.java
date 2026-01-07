package com.proveritus.cloudutility.observability.config;

import io.micrometer.tracing.Tracer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TracingConfiguration {

    @Bean
    public Tracer tracer() {
        return Tracer.NOOP;
    }
}