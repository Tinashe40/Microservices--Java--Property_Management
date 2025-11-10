package com.proveritus.userservice.userManager.mapper;

import com.proveritus.userservice.auth.dto.requests.SignUpRequest;
import com.proveritus.userservice.auth.domain.User;
import com.proveritus.userservice.userRoles.domain.Role;
import com.proveritus.userservice.userRoles.domain.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

public abstract class UserMapperDecorator implements UserMapper {

    @Autowired
    @Qualifier("delegate")
    private UserMapper delegate;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public User fromCreateDto(SignUpRequest createDto) {
        User user = delegate.fromCreateDto(createDto);
        user.setPassword(passwordEncoder.encode(createDto.getPassword()));

        Set<Role> roles = new HashSet<>();
        if (createDto.getRoles() == null || createDto.getRoles().isEmpty()) {
            roleRepository.findByName("USER").ifPresent(roles::add);
        } else {
            createDto.getRoles().forEach(roleName -> roleRepository.findByName(roleName.toUpperCase()).ifPresent(roles::add));
        }
        user.setRoles(roles);

        return user;
    }
}
