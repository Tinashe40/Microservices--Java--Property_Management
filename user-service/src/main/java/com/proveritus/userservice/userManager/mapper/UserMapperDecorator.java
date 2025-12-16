package com.proveritus.userservice.userManager.mapper;

import com.proveritus.cloudutility.dto.UserDTO;
import com.proveritus.userservice.auth.domain.User;

import com.proveritus.userservice.userGroups.domain.UserGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public abstract class UserMapperDecorator implements UserMapper {

    @Autowired
    @Qualifier("delegate")
    private UserMapper delegate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserGroupRepository userGroupRepository;

    @Override
    public UserDTO toDto(User user) {
        UserDTO userDTO = delegate.toDto(user);
            if (user.getUserGroups() != null) {
                Set<String> permissions = new HashSet<>();
                user.getUserGroups().forEach(userGroup -> userGroup.getPermissions().forEach(permission -> permissions.add(permission.getName())));
                if(user.getPermissions() != null){
                    user.getPermissions().forEach(permission -> permissions.add(permission.getName()));
                }
                userDTO.setPermissions(permissions);
            }
            return userDTO;
    }
}
