package com.proveritus.userservice.userManager.mapper;

import com.proveritus.userservice.auth.dto.requests.SignUpRequest;
import com.proveritus.userservice.auth.domain.User;
import com.proveritus.userservice.userRoles.mapper.RoleMapper;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
@DecoratedWith(UserMapperDecorator.class)
public interface UserMapper extends com.proveritus.cloudutility.jpa.EntityDtoMapper<com.proveritus.cloudutility.dto.UserDTO, User, SignUpRequest, com.proveritus.userservice.userManager.dto.UpdateUserDTO> {

    @org.mapstruct.Mapping(target = "id", source = "id")
    @org.mapstruct.Mapping(target = "username", source = "username")
    @org.mapstruct.Mapping(target = "email", source = "email")
    @org.mapstruct.Mapping(target = "firstName", source = "firstName")
    @org.mapstruct.Mapping(target = "lastName", source = "lastName")
    @org.mapstruct.Mapping(target = "phoneNumber", source = "phoneNumber")
    @org.mapstruct.Mapping(target = "roles", source = "roles")
    @org.mapstruct.Mapping(target = "enabled", source = "enabled")
    @org.mapstruct.Mapping(target = "accountNonExpired", source = "accountNonExpired")
    @org.mapstruct.Mapping(target = "accountNonLocked", source = "accountNonLocked")
    @org.mapstruct.Mapping(target = "credentialsNonExpired", source = "credentialsNonExpired")
    @org.mapstruct.Mapping(target = "permissions", ignore = true)
    com.proveritus.cloudutility.dto.UserDTO toDto(User user);

    @org.mapstruct.Mapping(target = "id", ignore = true)
    @org.mapstruct.Mapping(target = "createdBy", ignore = true)
    @org.mapstruct.Mapping(target = "createdDate", ignore = true)
    @org.mapstruct.Mapping(target = "lastModifiedBy", ignore = true)
    @org.mapstruct.Mapping(target = "lastModifiedDate", ignore = true)
    @org.mapstruct.Mapping(target = "version", ignore = true)
    @org.mapstruct.Mapping(target = "deleted", ignore = true)
    @org.mapstruct.Mapping(target = "enabled", ignore = true)
    @org.mapstruct.Mapping(target = "accountNonExpired", ignore = true)
    @org.mapstruct.Mapping(target = "accountNonLocked", ignore = true)
    @org.mapstruct.Mapping(target = "credentialsNonExpired", ignore = true)
    @org.mapstruct.Mapping(target = "password", ignore = true)
    @org.mapstruct.Mapping(target = "roles", ignore = true)
    User fromCreateDto(SignUpRequest createDto);

    @org.mapstruct.Mapping(target = "id", ignore = true)
    @org.mapstruct.Mapping(target = "username", ignore = true)
    @org.mapstruct.Mapping(target = "password", ignore = true)
    @org.mapstruct.Mapping(target = "roles", ignore = true)
    @org.mapstruct.Mapping(target = "createdBy", ignore = true)
    @org.mapstruct.Mapping(target = "createdDate", ignore = true)
    @org.mapstruct.Mapping(target = "lastModifiedBy", ignore = true)
    @org.mapstruct.Mapping(target = "lastModifiedDate", ignore = true)
    @org.mapstruct.Mapping(target = "version", ignore = true)
    @org.mapstruct.Mapping(target = "deleted", ignore = true)
    void updateFromUpdateDto(com.proveritus.userservice.userManager.dto.UpdateUserDTO updateDto, @MappingTarget User entity);
}