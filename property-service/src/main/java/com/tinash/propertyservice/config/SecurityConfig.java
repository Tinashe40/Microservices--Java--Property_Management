package com.tinash.propertyservice.config;

import com.tinash.cloud.utility.config.BaseSecurityConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true
)
@Import(BaseSecurityConfig.class)
@RequiredArgsConstructor
public class SecurityConfig {
    // All security configurations are now inherited from BaseSecurityConfig
}
