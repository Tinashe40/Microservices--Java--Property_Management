package com.proveritus.userservice.mapper;

import com.proveritus.cloudutility.dto.UserDTO;
import com.proveritus.userservice.DTO.SignUpRequest;
import com.proveritus.userservice.entity.Role;
import com.proveritus.userservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "roles", target = "roles", qualifiedByName = "rolesToString")
    UserDTO toDto(User user);

    @Mapping(target = "roles", ignore = true)
    User toEntity(UserDTO userDTO);

    List<User> toEntity(List<UserDTO> dtoList);

    List<UserDTO> toDto(List<User> entityList);

    UserDTO toDto(SignUpRequest signUpRequest);

    @Named("rolesToString")
    default Set<String> rolesToString(Set<Role> roles) {
        if (roles == null) {
            return null;
        }
        return roles.stream().map(Role::getName).collect(Collectors.toSet());
    }
}
