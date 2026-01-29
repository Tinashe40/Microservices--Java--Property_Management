package com.tinash.userservice.userGroups.mapper;

import com.tinash.cloud.utility.jpa.EntityDtoMapper;
import com.tinash.userservice.domain.model.permission.Permission;
import com.tinash.userservice.domain.model.usergroup.UserGroup;
import com.tinash.userservice.userGroups.dto.UserGroupDTO;
import org.mapstruct.*;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserGroupMapper extends EntityDtoMapper<UserGroupDTO, UserGroup, UserGroupDTO, UserGroupDTO> {

    @Mapping(target = "permissions", source = "permissions", qualifiedByName = "permissionsToPermissionNames")
    UserGroupDTO toDto(UserGroup entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "permissions", ignore = true) // Permissions are handled in the decorator
    UserGroup fromCreateDto(UserGroupDTO createDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "permissions", ignore = true) // Permissions are handled in the decorator
    void updateFromUpdateDto(UserGroupDTO updateDto, @MappingTarget UserGroup entity);

    default String userGroupToString(UserGroup userGroup) {
        return userGroup.getName();
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
