package com.proveritus.propertyservice.floor.docs;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FloorSwaggerDocumentation {
    @Bean
    public GroupedOpenApi floorSwaggerDocumentationAPIs(){
        return GroupedOpenApi.builder()
                .group("Floor APIs")
                .packagesToScan("com.proveritus.propertyservice.floor")
                .displayName("Floor APIs")
                .build();
    }
}
