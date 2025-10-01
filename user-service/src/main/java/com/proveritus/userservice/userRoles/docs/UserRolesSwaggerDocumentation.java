package com.proveritus.userservice.userRoles.docs;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserRolesSwaggerDocumentation {
    @Bean
    public GroupedOpenApi userRolesSwaggerAPIs() {
        return GroupedOpenApi.builder()
                .group("User Roles and Permissions")
                .displayName("User Roles and Permissions API")
                .packagesToScan("com.proveritus.userservice.userRoles.api")
                .build();
    }

}
