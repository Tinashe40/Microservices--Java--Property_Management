package com.proveritus.userservice.userManager.mapper;

import com.proveritus.userservice.auth.dto.requests.SignUpRequest;
import com.proveritus.userservice.auth.domain.User;
import com.proveritus.userservice.userRoles.domain.Role;
import com.proveritus.userservice.userRoles.domain.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

public abstract class UserMapperDecorator implements UserMapper {

    @Autowired
    @Qualifier("delegate")
    private UserMapper delegate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public com.proveritus.cloudutility.dto.UserDTO toDto(User user) {
        com.proveritus.cloudutility.dto.UserDTO userDTO = delegate.toDto(user);
        if (user.getRoles() != null) {
            Set<String> permissions = new HashSet<>();
            user.getRoles().forEach(role -> role.getPermissions().forEach(permission -> permissions.add(permission.getName())));
            userDTO.setPermissions(permissions);
        }
        return userDTO;
    }
}
