package com.proveritus.cloudutility.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "security")
@Data
public class SecurityProperties {

    private Cors cors = new Cors();
    private String[] publicEndpoints = new String[0];

    @Data
    public static class Cors {
        private List<String> allowedOrigins;
    }
}