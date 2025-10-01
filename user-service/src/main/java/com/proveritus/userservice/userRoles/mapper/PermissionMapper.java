package com.proveritus.userservice.userRoles.mapper;

import com.proveritus.cloudutility.jpa.EntityDtoMapper;
import com.proveritus.userservice.userRoles.domain.Permission;
import com.proveritus.userservice.userRoles.dto.PermissionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PermissionMapper extends EntityDtoMapper<PermissionDTO, Permission, PermissionDTO, PermissionDTO> {

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
