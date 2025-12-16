package com.proveritus.userservice.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Set<String> userGroups;
    private Set<String> permissions;
    private boolean enabled;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private LocalDateTime passwordLastChanged;
}
