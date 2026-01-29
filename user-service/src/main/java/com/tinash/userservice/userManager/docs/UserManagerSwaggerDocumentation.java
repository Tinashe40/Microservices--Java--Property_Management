package com.tinash.userservice.userManager.docs;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserManagerSwaggerDocumentation {
    @Bean
    public GroupedOpenApi userManagerSwaggerDocumentationApis() {
        return GroupedOpenApi.builder()
                .group("User Management APIs")
                .packagesToScan("com.proveritus.userservice.userManager")
                .build();
    }

}
