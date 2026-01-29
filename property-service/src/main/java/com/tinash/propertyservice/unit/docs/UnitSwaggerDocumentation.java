package com.tinash.propertyservice.unit.docs;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UnitSwaggerDocumentation {
    @Bean
    public GroupedOpenApi unitSwaggerDocumentationApi(){
        return GroupedOpenApi.builder()
                .group("Unit APIs")
                .displayName("Unit APIs")
                .packagesToScan("com.proveritus.propertyservice.unit")
                .build();
    }
}
