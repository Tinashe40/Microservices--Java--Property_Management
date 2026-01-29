package com.tinash.propertyservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableCaching
@EnableJpaRepositories(basePackages = {
    "com.proveritus.propertyservice.audit.domain",
    "com.proveritus.propertyservice.floor.domain",
    "com.proveritus.propertyservice.property.domain",
    "com.proveritus.propertyservice.unit.domain",
    "com.proveritus.propertyservice.property.repository",
    "com.proveritus.cloudutility.audit.repository"
})
@EntityScan(basePackages = {"com.proveritus.propertyservice", "com.proveritus.cloudutility"})
@OpenAPIDefinition(info = @Info(title = "Property Service", version = "v1"))
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)
@ComponentScan(
        basePackages = {"com.proveritus.propertyservice", "com.proveritus.cloudutility"}
)
public class PropertyServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PropertyServiceApplication.class, args);
    }

}