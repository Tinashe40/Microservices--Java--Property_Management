package com.proveritus.cloudutility.jpa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.proveritus.cloudutility.jpa.repository")
public class JpaConfiguration {
}