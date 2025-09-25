package com.proveritus.userservice.userManager.userRoles.mapper;

import com.proveritus.cloudutility.jpa.EntityDtoMapper;
import com.proveritus.userservice.userManager.userRoles.domain.Role;
import com.proveritus.userservice.userManager.userRoles.dto.CreateRoleDTO;
import com.proveritus.userservice.userManager.userRoles.dto.RoleDTO;
import com.proveritus.userservice.userManager.userRoles.dto.UpdateRoleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface RoleMapper extends EntityDtoMapper<RoleDTO, Role, CreateRoleDTO, UpdateRoleDTO> {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    Role fromCreateDto(CreateRoleDTO createDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateFromUpdateDto(UpdateRoleDTO updateDto, @MappingTarget Role entity);

    RoleDTO toDto(Role entity);
}
