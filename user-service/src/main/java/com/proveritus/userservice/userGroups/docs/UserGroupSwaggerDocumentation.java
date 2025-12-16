package com.proveritus.userservice.userGroups.docs;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserGroupSwaggerDocumentation {
    @Bean
    public GroupedOpenApi userGroupsSwaggerAPIs() {
        return GroupedOpenApi.builder()
                .group("User UserGroup and Permissions")
                .displayName("User UserGroup and Permissions API")
                .packagesToScan("com.proveritus.userservice.userGroups.api")
                .build();
    }

}
