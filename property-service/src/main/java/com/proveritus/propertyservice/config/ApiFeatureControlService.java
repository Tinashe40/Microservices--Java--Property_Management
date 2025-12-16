package com.proveritus.propertyservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service("apiFeatureControl")
@RequiredArgsConstructor
public class ApiFeatureControlService {

    private final Environment environment;

    public boolean isEnabled(String endpoint, String operation) {
        String property = String.format("api.endpoints.%s.%s.enabled", endpoint, operation);
        return environment.getProperty(property, Boolean.class, true);
    }
}
