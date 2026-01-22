package com.tinash.cloud.utility.security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.context.SecurityContext;

@Getter
@AllArgsConstructor
public class CustomSecurityContext {

    private final SecurityContext securityContext;
}
