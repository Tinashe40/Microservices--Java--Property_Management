package com.tinash.userservice.userManager.mapper;

import com.tinash.cloud.utility.dto.common.UserDto;
import com.tinash.userservice.domain.model.user.User;
import com.tinash.userservice.domain.repository.UserGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
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
    public UserDto toDto(User user) {
        UserDto UserDto = delegate.toDto(user);
            if (user.getUserGroups() != null) {
                Set<String> permissions = new HashSet<>();
                user.getUserGroups().forEach(userGroup -> userGroup.getPermissions().forEach(permission -> permissions.add(permission.getName())));
                if(user.getPermissions() != null){
                    user.getPermissions().forEach(permission -> permissions.add(permission.getName()));
                }
                UserDto.setPermissions((List<String>) permissions);
            }
            return UserDto;
    }
}
