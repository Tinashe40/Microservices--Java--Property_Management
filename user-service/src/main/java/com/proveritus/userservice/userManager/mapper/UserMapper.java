package com.proveritus.userservice.userManager.mapper;

import com.tinash.cloud.utility.dto.UserDto;
import com.tinash.cloud.utility.jpa.EntityDtoMapper;
import com.proveritus.userservice.auth.dto.requests.SignUpRequest;
import com.proveritus.userservice.auth.domain.User;
import com.proveritus.userservice.userManager.dto.UpdateUserDto;
import com.proveritus.userservice.userGroups.mapper.UserGroupMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {UserGroupMapper.class})
public interface UserMapper extends EntityDtoMapper<UserDto, User, SignUpRequest, UpdateUserDto> {

    @org.mapstruct.Mapping(target = "id", source = "id")
    @org.mapstruct.Mapping(target = "username", source = "username")
    @org.mapstruct.Mapping(target = "email", source = "email")
    @org.mapstruct.Mapping(target = "firstName", source = "firstName")
    @org.mapstruct.Mapping(target = "lastName", source = "lastName")
    @org.mapstruct.Mapping(target = "phoneNumber", source = "phoneNumber")
    @org.mapstruct.Mapping(target = "userGroups", source = "userGroups")
    @org.mapstruct.Mapping(target = "enabled", source = "enabled")
    @org.mapstruct.Mapping(target = "accountNonExpired", source = "accountNonExpired")
    @org.mapstruct.Mapping(target = "accountNonLocked", source = "accountNonLocked")
    @org.mapstruct.Mapping(target = "credentialsNonExpired", source = "credentialsNonExpired")
    @org.mapstruct.Mapping(target = "permissions", ignore = true)
    UserDto toDto(User user);

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
    @org.mapstruct.Mapping(target = "userGroups", ignore = true)
    @org.mapstruct.Mapping(target = "tokens", ignore = true)
    @org.mapstruct.Mapping(target = "permissions", ignore = true)
    @org.mapstruct.Mapping(target = "passwordLastChanged", ignore = true)
    User fromCreateDto(SignUpRequest createDto);

    @org.mapstruct.Mapping(target = "id", ignore = true)
    @org.mapstruct.Mapping(target = "username", ignore = true)
    @org.mapstruct.Mapping(target = "password", ignore = true)
    @org.mapstruct.Mapping(target = "userGroups", ignore = true)
    @org.mapstruct.Mapping(target = "tokens", ignore = true)
    @org.mapstruct.Mapping(target = "permissions", ignore = true)
    @org.mapstruct.Mapping(target = "createdBy", ignore = true)
    @org.mapstruct.Mapping(target = "createdDate", ignore = true)
    @org.mapstruct.Mapping(target = "lastModifiedBy", ignore = true)
    @org.mapstruct.Mapping(target = "lastModifiedDate", ignore = true)
    @org.mapstruct.Mapping(target = "version", ignore = true)
    @org.mapstruct.Mapping(target = "deleted", ignore = true)
    @org.mapstruct.Mapping(target = "passwordLastChanged", ignore = true)
    @org.mapstruct.Mapping(target = "phoneNumber", ignore = true)
    @org.mapstruct.Mapping(target = "enabled", ignore = true)
    @org.mapstruct.Mapping(target = "accountNonExpired", ignore = true)
    @org.mapstruct.Mapping(target = "accountNonLocked", ignore = true)
    @org.mapstruct.Mapping(target = "credentialsNonExpired", ignore = true)
    void updateFromUpdateDto(UpdateUserDto updateDto, @MappingTarget User entity);
}