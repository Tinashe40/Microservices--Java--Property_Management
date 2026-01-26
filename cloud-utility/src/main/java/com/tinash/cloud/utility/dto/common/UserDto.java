package com.tinash.cloud.utility.dto.common;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDto {
    private String id;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Set<String> userGroups;
    private LocalDateTime passwordLastChanged;
    private String username;
    private String email;
    private List<String> permissions;

    @Builder.Default
    private boolean enabled = true;

    @Builder.Default
    private boolean accountNonExpired = true;

    @Builder.Default
    private boolean accountNonLocked = true;

    @Builder.Default
    private boolean credentialsNonExpired = true;
}