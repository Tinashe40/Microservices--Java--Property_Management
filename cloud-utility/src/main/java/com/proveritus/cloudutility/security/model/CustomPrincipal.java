package com.proveritus.cloudutility.security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.security.Principal;

@Getter
@AllArgsConstructor
public class CustomPrincipal implements Principal {

    private final String name;
}