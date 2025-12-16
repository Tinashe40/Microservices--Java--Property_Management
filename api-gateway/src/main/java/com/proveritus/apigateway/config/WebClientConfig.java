package com.proveritus.apigateway.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * WebClient configuration for load-balanced service-to-service communication.
 */
@Configuration
public class WebClientConfig {

    /**
     * Creates a load-balanced WebClient builder for microservice communication.
     *
     * @return configured WebClient builder with load balancing
     */
    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }
}