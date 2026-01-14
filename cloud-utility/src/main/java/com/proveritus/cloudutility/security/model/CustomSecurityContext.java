package com.proveritus.cloudutility.security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.context.SecurityContext;

@Getter
@AllArgsConstructor
public class CustomSecurityContext {

    private final org.springframework.security.core.context.SecurityContext securityContext;
}
