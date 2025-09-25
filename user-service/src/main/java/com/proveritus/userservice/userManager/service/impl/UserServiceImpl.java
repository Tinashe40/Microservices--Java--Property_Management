package com.proveritus.userservice.userManager.service.impl;

import com.proveritus.cloudutility.dto.UserDTO;
import com.proveritus.cloudutility.jpa.DomainServiceImpl;
import com.proveritus.cloudutility.security.CustomPrincipal;
import com.proveritus.cloudutility.validator.UserValidator;
import com.proveritus.userservice.Auth.DTO.SignUpRequest;
import com.proveritus.userservice.Auth.domain.User;
import com.proveritus.userservice.userManager.domain.UserRepository;
import com.proveritus.userservice.userManager.dto.UpdateUserDTO;
import com.proveritus.userservice.userManager.mapper.UserMapper;
import com.proveritus.userservice.userManager.service.UserService;
import com.proveritus.userservice.userManager.userRoles.domain.Role;
import com.proveritus.userservice.userManager.userRoles.domain.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@Transactional
public class UserServiceImpl extends DomainServiceImpl<User, SignUpRequest, UpdateUserDTO, UserDTO> implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserValidator userValidator;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, UserValidator userValidator, RoleRepository roleRepository) {
        super(userRepository, userMapper);
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userValidator = userValidator;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDTO registerUser(SignUpRequest signUpRequest) {
        log.debug("Request to register user : {}", signUpRequest.getUsername());

        userValidator.validate(userMapper.toDto(signUpRequest), userRepository.existsByUsername(signUpRequest.getUsername()), userRepository.existsByEmail(signUpRequest.getEmail()));

        User user = userMapper.fromCreateDto(signUpRequest);

        Set<Role> roles = new HashSet<>();
        if (signUpRequest.getRoles() == null || signUpRequest.getRoles().isEmpty()) {
            roleRepository.findByName("VIEWER").ifPresent(roles::add);
        } else {
            signUpRequest.getRoles().forEach(roleName -> roleRepository.findByName(roleName.toUpperCase()).ifPresent(roles::add));
        }
        user.setRoles(roles);

        User savedUser = userRepository.save(user);
        log.debug("Saved user : {}", savedUser);

        return userMapper.toDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getCurrentUser() {
        CustomPrincipal userPrincipal = (CustomPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return findById(userPrincipal.getId());
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "users", key = "#username")
    public Optional<UserDTO> getUserByUsername(String username) {
        log.debug("Request to get User : {}", username);
        return userRepository.findByUsername(username).map(userMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDTO> getUsersByIds(List<Long> ids) {
        log.debug("Request to get Users by IDs: {}", ids);
        return userMapper.toDto(userRepository.findAllById(ids));
    }

    @Override
    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(Long id) {
        log.debug("Request to delete User : {}", id);
        this.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isCurrentUser(Long id) {
        CustomPrincipal userPrincipal = (CustomPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userPrincipal.getId().equals(id);
    }

    @Override
    @CacheEvict(value = "users", key = "#userId")
    public UserDTO assignRolesToUser(Long userId, Set<String> roleNames) {
        log.debug("Request to assign roles to user : {}", userId);
        User user = findEntityById(userId);

        Set<Role> roles = new HashSet<>();
        roleNames.forEach(roleName -> roleRepository.findByName(roleName.toUpperCase()).ifPresent(roles::add));
        user.setRoles(roles);

        User result = userRepository.save(user);
        log.debug("Updated user roles : {}", result);

        return userMapper.toDto(result);
    }

    @Override
    public Class<User> getEntityClass() {
        return User.class;
    }
}