package com.proveritus.userservice.userManager.service.impl;

import com.tinash.cloud.utility.audit.annotation.Auditable;
import com.tinash.cloud.utility.dto.UserDto;
import com.tinash.cloud.utility.exception.RegistrationException;
import com.tinash.cloud.utility.exception.ResourceNotFoundException;
import com.tinash.cloud.utility.jpa.DomainServiceImpl;
import com.tinash.cloud.utility.password.dto.ResetPasswordRequest;
import com.proveritus.userservice.auth.domain.User;
import com.proveritus.userservice.auth.dto.requests.SignUpRequest;
import com.proveritus.userservice.email.EmailService;
import com.proveritus.userservice.passwordManager.service.PasswordService;
import com.proveritus.userservice.userGroups.domain.Permission;
import com.proveritus.userservice.userGroups.domain.PermissionRepository;
import com.proveritus.userservice.userManager.domain.UserRepository;
import com.proveritus.userservice.userManager.dto.UpdateUserDto;
import com.proveritus.userservice.userManager.mapper.UserMapper;
import com.proveritus.userservice.userManager.service.UserService;
import com.proveritus.userservice.userManager.validation.UserValidator;
import com.proveritus.userservice.userGroups.domain.UserGroup;
import com.proveritus.userservice.userGroups.domain.UserGroupRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.proveritus.cloudutility.security.util.SecurityUtils.getCurrentUserId;
import static com.proveritus.cloudutility.security.util.SecurityUtils.getCurrentUsername;

@Slf4j
@Service
@Transactional
public class UserServiceImpl extends DomainServiceImpl<User, SignUpRequest, UpdateUserDto, UserDto> implements UserService {
    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final UserMapper userMapper;
    private final UserGroupRepository userGroupRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final UserValidator userValidator;
    private final PermissionRepository permissionRepository;
    private final PasswordService passwordService;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, UserGroupRepository userGroupRepository, PasswordEncoder passwordEncoder, EmailService emailService, UserValidator userValidator, PermissionRepository permissionRepository, PasswordService passwordService) {
        super(userRepository, userMapper);
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userGroupRepository = userGroupRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.userValidator = userValidator;
        this.permissionRepository = permissionRepository;
        this.passwordService = passwordService;
    }



    @Override
    @Auditable(action = "REGISTER_USER", entity = "User")
    public UserDto registerUser(SignUpRequest signUpRequest) {
        log.debug("Request to register user : {}", signUpRequest.getUsername());

        try {
            userValidator.validate(null, userRepository.existsByUsernameAndDeletedFalse(signUpRequest.getUsername()), userRepository.existsByEmailAndDeletedFalse(signUpRequest.getEmail()));
        } catch (IllegalArgumentException e) {
            throw new RegistrationException(e.getMessage());
        }

        User user = userMapper.fromCreateDto(signUpRequest);
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setPasswordLastChanged(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        log.debug("Saved user : {}", savedUser);

        return userMapper.toDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getCurrentUser() {
        String username = getCurrentUsername()
                .orElseThrow(() -> new ResourceNotFoundException("User not found in security context"));
        return getUserByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "users", key = "#username")
    public Optional<UserDto> getUserByUsername(String username) {
        log.debug("Request to get User : {}", username);
        return userRepository.findByUsernameAndDeletedFalse(username).map(userMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsersByIds(List<Long> ids) {
        log.debug("Request to get Users by IDs: {}", ids);
        return userRepository.findAllById(ids).stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    @Auditable(action = "DELETE_USER", entity = "User")
    public void deleteById(Long id) {
        log.debug("Request to delete User : {}", id);
        super.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isCurrentUser(Long id) {
        return getCurrentUserId()
                .map(currentUserId -> currentUserId.equals(id))
                .orElse(false);
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    @Auditable(action = "ASSIGN_GROUPS", entity = "User")
    public UserDto assignUserGroupsToUser(Long userId, Set<String> groupNames) {
        log.debug("Request to assign groups to user : {}", userId);
        User user = findEntityById(userId);

        Set<String> upperCaseGroupNames = groupNames.stream().map(String::toUpperCase).collect(Collectors.toSet());
        Set<UserGroup> userGroups = userGroupRepository.findByNameIn(upperCaseGroupNames);

        if (userGroups.size() != groupNames.size()) {
            log.warn("Could not find all groups for user {}", userId);
        }
        user.setUserGroups(userGroups);

        User result = userRepository.save(user);
        log.debug("Updated user groups : {}", result);

        return userMapper.toDto(result);
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    @Auditable(action = "ASSIGN_PERMISSIONS", entity = "User")
    public UserDto assignPermissionsToUser(Long userId, Set<String> permissionNames) {
        log.debug("Request to assign permissions to user : {}", userId);
        User user = findEntityById(userId);

        Set<Permission> permissions = permissionRepository.findByNameIn(permissionNames);
        if (permissions.size() != permissionNames.size()) {
            log.warn("Could not find all permissions for user {}", userId);
        }

        user.getPermissions().addAll(permissions);
        User result = userRepository.save(user);
        log.debug("Updated user permissions : {}", result);

        return userMapper.toDto(result);
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    @Auditable(action = "REMOVE_PERMISSIONS", entity = "User")
    public UserDto removePermissionsFromUser(Long userId, Set<String> permissionNames) {
        log.debug("Request to remove permissions from user : {}", userId);
        User user = findEntityById(userId);

        Set<Permission> permissionsToRemove = user.getPermissions().stream()
                .filter(permission -> permissionNames.contains(permission.getName()))
                .collect(Collectors.toSet());

        user.getPermissions().removeAll(permissionsToRemove);
        User result = userRepository.save(user);
        log.debug("Updated user permissions : {}", result);

        return userMapper.toDto(result);
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    @Auditable(action = "DEACTIVATE_USER", entity = "User")
    public void deactivateUser(Long id) {
        log.debug("Request to deactivate User : {}", id);
        User user = findEntityById(id);
        user.setEnabled(false);
        userRepository.save(user);
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    @Auditable(action = "ACTIVATE_USER", entity = "User")
    public void activateUser(Long id) {
        log.debug("Request to activate User : {}", id);
        User user = findEntityById(id);
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    @Auditable(action = "RESET_PASSWORD", entity = "User")
    public void resetPassword(Long id, ResetPasswordRequest request) {
        passwordService.adminResetPassword(id, request);
    }

    @Override
    public long countAllUsers() {
        return userRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserEntityById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Override
    public Class<User> getEntityClass() {
        return User.class;
    }
}

