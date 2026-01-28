package com.proveritus.userservice.userManager.service.impl;

import com.tinash.cloud.utility.audit.annotation.Auditable;
import com.tinash.cloud.utility.dto.UserDto;
import com.tinash.cloud.utility.exception.RegistrationException;
import com.tinash.cloud.utility.exception.ResourceNotFoundException;
import com.tinash.cloud.utility.jpa.DomainServiceImpl;
import com.proveritus.userservice.passwordManager.password.dto.ResetPasswordRequest;
import com.tinash.cloud.utility.security.password.CustomPasswordEncoder;
import com.proveritus.userservice.domain.model.user.User;
import com.proveritus.userservice.auth.dto.requests.SignUpRequest;
import com.proveritus.userservice.email.EmailService;
import com.proveritus.userservice.passwordManager.service.PasswordService;
import com.proveritus.userservice.domain.model.permission.Permission;
import com.proveritus.userservice.domain.repository.PermissionRepository;
import com.proveritus.userservice.domain.repository.UserRepository;
import com.proveritus.userservice.userManager.dto.UpdateUserDto;
import com.proveritus.userservice.userManager.mapper.UserMapper;
import com.proveritus.userservice.userManager.service.UserService;
import com.proveritus.userservice.userManager.validation.UserValidator;
import com.proveritus.userservice.domain.model.usergroup.UserGroup;
import com.proveritus.userservice.domain.repository.UserGroupRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserGroupRepository userGroupRepository;
    private final CustomPasswordEncoder customPasswordEncoder;
    private final EmailService emailService;
    private final UserValidator userValidator;
    private final PermissionRepository permissionRepository;
    private final PasswordService passwordService;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, UserGroupRepository userGroupRepository, CustomPasswordEncoder customPasswordEncoder, EmailService emailService, UserValidator userValidator, PermissionRepository permissionRepository, PasswordService passwordService) {
        super(userRepository, userMapper);
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.userGroupRepository = userGroupRepository;
        this.customPasswordEncoder = customPasswordEncoder;
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
            boolean usernameExists = userRepository.existsByUserProfile_UsernameAndDeletedFalse(
                    signUpRequest.getUsername());
            boolean emailExists = userRepository.existsByEmail_ValueAndDeletedFalse(signUpRequest.getEmail());
            userValidator.validate(null, usernameExists, emailExists);
        } catch (IllegalArgumentException e) {
            throw new RegistrationException(e.getMessage());
        }

        User user = new User(
                signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                signUpRequest.getPassword(),
                signUpRequest.getFirstName(),
                signUpRequest.getLastName(),
                signUpRequest.getPhoneNumber(),
                customPasswordEncoder
        );
        User savedUser = userRepository.save(user);
        log.debug("Saved user : {}", savedUser);

        return userMapper.toDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getCurrentUser() {
        String username = getCurrentUsername()
                .orElseThrow(() -> new ResourceNotFoundException("User not found in security context"));
        return findByUsername(username) // Call the new findByUsername method
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "users", key = "#email") // Changed key to email as username is derived
    public Optional<UserDto> getUserByEmail(String email) {
        log.debug("Request to get User by email : {}", email);
        return userRepository.findByEmail_ValueAndDeletedFalse(email).map(userMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "users", key = "#username")
    public Optional<UserDto> findByUsername(String username) {
        log.debug("Request to get User by username : {}", username);
        return userRepository.findByUserProfile_UsernameAndDeletedFalse(username)
                .map(userMapper::toDto);
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
        user.getUserGroups().clear(); // Clear existing groups
        userGroups.forEach(user::assignUserGroup); // Use the domain method

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

        permissions.forEach(user::assignPermission); // Use the domain method
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

        Set<Permission> permissionsToRemove = permissionRepository.findByNameIn(permissionNames);
        permissionsToRemove.forEach(user::removePermission); // Use the domain method

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
        user.deactivate(); // Use the domain method
        userRepository.save(user);
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    @Auditable(action = "ACTIVATE_USER", entity = "User")
    public void activateUser(Long id) {
        log.debug("Request to activate User : {}", id);
        User user = findEntityById(id);
        user.activate(); // Use the domain method
        userRepository.save(user);
    }

    @Override
    @Auditable(action = "RESET_PASSWORD", entity = "User")
    public void resetPassword(Long id, ResetPasswordRequest request) {
        User user = findEntityById(id);
        user.changePassword(request.getNewPassword(), customPasswordEncoder); // Use the domain method
        userRepository.save(user); // Save the user after password reset
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

