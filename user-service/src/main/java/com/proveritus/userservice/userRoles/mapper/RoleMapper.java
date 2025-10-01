package com.proveritus.userservice.userRoles.mapper;

import com.proveritus.cloudutility.jpa.EntityDtoMapper;
import com.proveritus.userservice.userRoles.domain.Permission;
import com.proveritus.userservice.userRoles.domain.Role;
import com.proveritus.userservice.userRoles.dto.RoleDTO;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
@DecoratedWith(RoleMapperDecorator.class)
public interface RoleMapper extends EntityDtoMapper<RoleDTO, Role, RoleDTO, RoleDTO> {

    @Mapping(target = "permissions", source = "permissions", qualifiedByName = "permissionsToPermissionNames")
    RoleDTO toDto(Role entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "permissions", ignore = true) // Permissions are handled in the decorator
    Role fromCreateDto(RoleDTO createDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "permissions", ignore = true) // Permissions are handled in the decorator
    void updateFromUpdateDto(RoleDTO updateDto, @MappingTarget Role entity);

    default String roleToString(Role role) {
        return role.getName();
    }

    @Named("permissionsToPermissionNames")
    default Set<String> permissionsToPermissionNames(Set<Permission> permissions) {
        if (permissions == null) {
            return null;
        }
        return permissions.stream()
                .map(Permission::getName)
                .collect(Collectors.toSet());
    }
}
