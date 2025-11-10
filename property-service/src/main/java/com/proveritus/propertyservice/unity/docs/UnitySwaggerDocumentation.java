package com.proveritus.propertyservice.unity.docs;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UnitySwaggerDocumentation {
    @Bean
    public GroupedOpenApi unitySwaggerDocumentationApi(){
        return GroupedOpenApi.builder()
                .group("Unity Apis")
                .displayName("Unity APIs")
                .packagesToScan("com.proveritus.propertyservice.unity")
                .build();
    }
}
