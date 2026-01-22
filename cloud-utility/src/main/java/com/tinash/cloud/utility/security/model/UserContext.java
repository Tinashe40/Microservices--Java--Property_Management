package com.tinash.cloud.utility.security.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
@AllArgsConstructor
public class UserContext {

    private final String username;
    private final Collection<? extends GrantedAuthority> authorities;
}
