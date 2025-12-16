package com.proveritus.userservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.*;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.*;
import org.springframework.mail.javamail.*;

import java.util.Properties;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
@EnableCaching
@OpenAPIDefinition(info = @Info(title = "User Service", version = "v1"))
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)
@ComponentScan(basePackages = {"com.proveritus.userservice", "com.proveritus.cloudutility"})
@EnableJpaRepositories(basePackages = {
    "com.proveritus.userservice.userManager.domain",
    "com.proveritus.userservice.userGroups.domain",
    "com.proveritus.userservice.jwt",
    "com.proveritus.cloudutility.audit.repository",
    "com.proveritus.userservice.passwordManager.domain.repo"
})
@EntityScan(basePackages = {"com.proveritus.userservice", "com.proveritus.cloudutility"})
public class UserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("sandbox.smtp.mailtrap.io");
        mailSender.setPort(2525);
        mailSender.setUsername("9d2dee80537395");
        mailSender.setPassword("9ebac56779b316");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
