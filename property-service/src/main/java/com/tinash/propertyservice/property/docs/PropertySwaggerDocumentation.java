package com.tinash.propertyservice.property.docs;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PropertySwaggerDocumentation {
    @Bean
    public GroupedOpenApi propertySwaggerDocumentationApis() {
        return GroupedOpenApi.builder()
                .group("Property APIs")
                .displayName("Property APIs")
                .packagesToScan("com.proveritus.propertyservice.property")
                .build();
    }
}
