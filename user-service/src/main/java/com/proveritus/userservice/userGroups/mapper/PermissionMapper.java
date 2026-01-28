package com.proveritus.userservice.userGroups.mapper;


import com.proveritus.userservice.domain.model.permission.Permission;
import com.proveritus.userservice.userGroups.dto.PermissionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PermissionMapper extends com.tinash.cloud.utility.mapper.BaseMapper<PermissionDTO, Permission> {

    PermissionDTO toDto(Permission entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Permission fromCreateDto(PermissionDTO createDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateFromUpdateDto(PermissionDTO updateDto, @MappingTarget Permission entity);
}
