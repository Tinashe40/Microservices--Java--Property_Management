package com.tinash.userservice.userGroups.mapper;

import com.tinash.userservice.domain.model.permission.Permission;
import com.tinash.userservice.domain.repository.PermissionRepository;
import com.tinash.userservice.domain.model.usergroup.UserGroup;
import com.tinash.userservice.userGroups.dto.UserGroupDTO;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Set;

@Component
public abstract class UserGroupMapperDecorator implements UserGroupMapper {

    @Autowired
    @Qualifier("delegate")
    private UserGroupMapper delegate;

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public UserGroup fromCreateDto(UserGroupDTO createDto) {
        UserGroup userGroup = delegate.fromCreateDto(createDto);
        if (!CollectionUtils.isEmpty(createDto.getPermissions())) {
            Set<Permission> permissions = permissionRepository.findByNameIn(createDto.getPermissions());
            userGroup.setPermissions(permissions);
        }
        return userGroup;
    }

    @Override
    public void updateFromUpdateDto(UserGroupDTO updateDto, @MappingTarget UserGroup entity) {
        delegate.updateFromUpdateDto(updateDto, entity);
        if (updateDto.getPermissions() == null) {
            entity.setPermissions(new HashSet<>());
        } else {
            Set<Permission> permissions = permissionRepository.findByNameIn(updateDto.getPermissions());
            entity.setPermissions(permissions);
        }
    }
}
