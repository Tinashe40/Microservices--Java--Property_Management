package com.tinash.cloud.utility.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * General Application Configuration.
 * This class provides common beans and configurations that are not specific to web or JPA auditing.
 */
@Configuration
public class AppConfig {

    /**
     * Configures a RestTemplate bean.
     * RestTemplate is a synchronous HTTP client for making RESTful API calls.
     * While FeignClient is preferred for internal microservice communication,
     * RestTemplate can be useful for interacting with external APIs or simple HTTP calls.
     *
     * @return a configured RestTemplate instance.
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
