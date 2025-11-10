package com.proveritus.userservice.auth.docs;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthSwaggerDocumentation {

    @Bean
    public GroupedOpenApi authSwaggerDocumentationApis() {
        return GroupedOpenApi.builder()
                .group("Auth APIs")
                .packagesToScan("com.proveritus.userservice.auth")
                .build();
    }
}
