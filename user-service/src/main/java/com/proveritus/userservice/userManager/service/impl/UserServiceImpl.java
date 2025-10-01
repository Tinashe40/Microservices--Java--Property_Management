package com.proveritus.userservice.userManager.service.impl;

import com.proveritus.cloudutility.dto.UserDTO;
import com.proveritus.cloudutility.jpa.DomainServiceImpl;
import com.proveritus.cloudutility.security.CustomPrincipal;
import com.proveritus.userservice.Auth.DTO.SignUpRequest;
import com.proveritus.userservice.Auth.domain.User;
import com.proveritus.cloudutility.exception.RegistrationException;
import com.proveritus.userservice.userManager.domain.UserRepository;
import com.proveritus.userservice.userManager.dto.UpdateUserDTO;
import com.proveritus.userservice.userManager.mapper.UserMapper;
import com.proveritus.userservice.userManager.service.UserService;
import com.proveritus.userservice.userRoles.domain.Role;
import com.proveritus.userservice.userRoles.domain.RoleRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class UserServiceImpl extends DomainServiceImpl<User, SignUpRequest, UpdateUserDTO, UserDTO> implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, RoleRepository roleRepository) {
        super(userRepository, userMapper);
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDTO registerUser(SignUpRequest signUpRequest) {
        log.debug("Request to register user : {}", signUpRequest.getUsername());

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new RegistrationException("Username is already taken!");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new RegistrationException("Email is already in use!");
        }

        User user = userMapper.fromCreateDto(signUpRequest);
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
    @CacheEvict(value = "users", allEntries = true) // Evict all users cache on delete
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
    @CacheEvict(value = "users", allEntries = true) // Evict all users cache on role change
    public UserDTO assignRolesToUser(Long userId, Set<String> roleNames) {
        log.debug("Request to assign roles to user : {}", userId);
        User user = findEntityById(userId);

        Set<String> upperCaseRoleNames = roleNames.stream().map(String::toUpperCase).collect(Collectors.toSet());
        Set<Role> roles = roleRepository.findByNameIn(upperCaseRoleNames);

        if (roles.size() != roleNames.size()) {
            log.warn("Could not find all roles for user {}", userId);
        }
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
