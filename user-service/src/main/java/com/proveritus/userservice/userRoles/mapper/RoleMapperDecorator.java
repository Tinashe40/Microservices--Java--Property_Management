package com.proveritus.userservice.userRoles.mapper;

import com.proveritus.userservice.userRoles.domain.Permission;
import com.proveritus.userservice.userRoles.domain.PermissionRepository;
import com.proveritus.userservice.userRoles.domain.Role;
import com.proveritus.userservice.userRoles.dto.RoleDTO;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Set;

public abstract class RoleMapperDecorator implements RoleMapper {

    @Autowired
    @Qualifier("delegate")
    private RoleMapper delegate;

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public Role fromCreateDto(RoleDTO createDto) {
        Role role = delegate.fromCreateDto(createDto);
        if (!CollectionUtils.isEmpty(createDto.getPermissions())) {
            Set<Permission> permissions = permissionRepository.findByNameIn(createDto.getPermissions());
            role.setPermissions(permissions);
        }
        return role;
    }

    @Override
    public void updateFromUpdateDto(RoleDTO updateDto, @MappingTarget Role entity) {
        delegate.updateFromUpdateDto(updateDto, entity);
        if (updateDto.getPermissions() == null) {
            entity.setPermissions(new HashSet<>());
        } else {
            Set<Permission> permissions = permissionRepository.findByNameIn(updateDto.getPermissions());
            entity.setPermissions(permissions);
        }
    }
}
