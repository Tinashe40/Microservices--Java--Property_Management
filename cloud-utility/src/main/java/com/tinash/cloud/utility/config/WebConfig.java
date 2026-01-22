package com.tinash.cloud.utility.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Global Web MVC Configuration.
 * Configures CORS (Cross-Origin Resource Sharing) policies for the application.
 * Allows requests from any origin, with specified methods and headers.
 * In a production environment, restrict origins to known frontend URLs.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Apply to all endpoints
                .allowedOrigins("*") // Allow requests from any origin (for development)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed HTTP methods
                .allowedHeaders("*") // Allow all headers
                .allowCredentials(true) // Allow sending credentials like cookies (if applicable)
                .maxAge(3600); // Max age of the CORS pre-flight request result
    }
}
