package com.tinash.cloud.utility.security.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "security")
@Getter
@Setter
public class SecurityProperties {

    private Cors cors = new Cors();
    private String[] publicEndpoints = new String[0];

    @Getter
    @Setter
    public static class Cors {
        private List<String> allowedOrigins;
    }
}
