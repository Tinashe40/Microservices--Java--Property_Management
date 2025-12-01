package com.proveritus.userservice.userManager.service.impl;

import com.proveritus.cloudutility.dto.UserDTO;
import com.proveritus.cloudutility.jpa.DomainServiceImpl;
import com.proveritus.userservice.auth.dto.requests.SignUpRequest;
import com.proveritus.userservice.auth.domain.User;
import com.proveritus.cloudutility.exception.RegistrationException;
import com.proveritus.userservice.userManager.domain.UserRepository;
import com.proveritus.userservice.userManager.dto.ResetPasswordRequest;
import com.proveritus.userservice.userManager.dto.UpdateUserDTO;
import com.proveritus.userservice.userManager.mapper.UserMapper;
import com.proveritus.userservice.userManager.service.UserService;
import com.proveritus.userservice.userRoles.domain.Role;
import com.proveritus.userservice.userRoles.domain.RoleRepository;
import com.proveritus.userservice.email.EmailService;
import com.proveritus.cloudutility.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, RoleRepository roleRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        super(userRepository, userMapper);
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    public UserDTO registerUser(SignUpRequest signUpRequest) {
        log.debug("Request to register user : {}", signUpRequest.getUsername());

        if (userRepository.existsByUsernameAndDeletedFalse(signUpRequest.getUsername())) {
            throw new RegistrationException("Username is already taken!");
        }

        if (userRepository.existsByEmailAndDeletedFalse(signUpRequest.getEmail())) {
            throw new RegistrationException("Email is already in use!");
        }

        User user = userMapper.fromCreateDto(signUpRequest);
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        User savedUser = userRepository.save(user);
        log.debug("Saved user : {}", savedUser);

        return userMapper.toDto(savedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        return getUserByUsername(username)
                .orElseThrow(() -> new com.proveritus.cloudutility.exception.UserNotFoundException("User not found with username: " + username));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "users", key = "#username")
    public Optional<UserDTO> getUserByUsername(String username) {
        log.debug("Request to get User : {}", username);
        return userRepository.findByUsernameAndDeletedFalse(username).map(userMapper::toDto);
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
        User user = findEntityById(id);
        user.setDeleted(true);
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isCurrentUser(Long id) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        return userRepository.findByUsernameAndDeletedFalse(username).map(user -> user.getId().equals(id)).orElse(false);
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
    public Page<UserDTO> findAll(Pageable pageable) {
        return userRepository.findAllByDeletedFalse(pageable).map(userMapper::toDto);
    }

    @Override
    public UserDTO findById(Long id) {
        return userRepository.findByIdAndDeletedFalse(id).map(userMapper::toDto)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    public void deactivateUser(Long id) {
        log.debug("Request to deactivate User : {}", id);
        User user = findEntityById(id);
        user.setEnabled(false);
        userRepository.save(user);
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    public void activateUser(Long id) {
        log.debug("Request to activate User : {}", id);
        User user = findEntityById(id);
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Override
    public void changePassword(com.proveritus.userservice.userManager.dto.ChangePasswordRequest request) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        User user = userRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        emailService.sendPasswordChangedEmail(user.getEmail(), user.getUsername());
    }

    public void resetPassword(Long id, ResetPasswordRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public long countAllUsers() {
        return userRepository.count();
    }

    @Override
    public Class<User> getEntityClass() {
        return User.class;
    }
}

