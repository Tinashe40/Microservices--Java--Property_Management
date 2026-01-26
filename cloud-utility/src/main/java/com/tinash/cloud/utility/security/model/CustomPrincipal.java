package com.tinash.cloud.utility.security.model;

import lombok.Data;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.util.Collection;


@Getter
public class CustomPrincipal extends User {

    private final String id;
    private final String email;

    public CustomPrincipal(String id,
                           String username,
                           String email,
                           String password,
                           Collection<? extends GrantedAuthority> authorities,
                           boolean enabled,
                           boolean accountNonExpired,
                           boolean accountNonLocked,
                           boolean credentialsNonExpired) {
        super(username,
                password,
                enabled,
                accountNonExpired,
                credentialsNonExpired,
                accountNonLocked,
                authorities);
        this.id = id;
        this.email = email;
    }
}
