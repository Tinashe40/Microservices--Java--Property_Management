package com.proveritus.userservice.userManager.mapper;

import com.proveritus.cloudutility.dto.UserDTO;
import com.proveritus.cloudutility.jpa.EntityDtoMapper;
import com.proveritus.userservice.Auth.DTO.SignUpRequest;
import com.proveritus.userservice.Auth.domain.User;
import com.proveritus.userservice.userManager.dto.UpdateUserDTO;
import com.proveritus.userservice.userManager.userRoles.domain.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class UserMapper implements EntityDtoMapper<UserDTO, User, SignUpRequest, UpdateUserDTO> {

    @Autowired
    protected PasswordEncoder passwordEncoder;

    @Mapping(source = "roles", target = "roles", qualifiedByName = "rolesToString")
    public abstract UserDTO toDto(User user);

    public abstract List<UserDTO> toDto(List<User> user);

    public abstract UserDTO toDto(SignUpRequest signUpRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "accountNonExpired", ignore = true)
    @Mapping(target = "accountNonLocked", ignore = true)
    @Mapping(target = "credentialsNonExpired", ignore = true)
    @Mapping(target = "password", expression = "java(passwordEncoder.encode(createDto.getPassword()))")
    @Mapping(target = "roles", ignore = true) // Roles are handled in the service layer after creation
    public abstract User fromCreateDto(SignUpRequest createDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "username", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true) // Roles are handled separately in the service
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    public abstract void updateFromUpdateDto(UpdateUserDTO updateDto, @MappingTarget User entity);

    @Named("rolesToString")
    protected Set<String> rolesToString(Set<Role> roles) {
        if (roles == null) {
            return null;
        }
        return roles.stream().map(Role::getName).collect(Collectors.toSet());
    }
}
