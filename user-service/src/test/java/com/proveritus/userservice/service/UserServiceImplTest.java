package com.proveritus.userservice.service;

import com.proveritus.cloudutility.dto.UserDTO;
import com.proveritus.cloudutility.exception.EntityNotFoundException;
import com.proveritus.cloudutility.validator.UserValidator;
import com.proveritus.userservice.Auth.DTO.SignUpRequest;
import com.proveritus.userservice.Auth.domain.User;
import com.proveritus.userservice.userManager.domain.UserRepository;
import com.proveritus.userservice.userManager.mapper.UserMapper;
import com.proveritus.userservice.userManager.service.impl.UserServiceImpl;
import com.proveritus.userservice.userManager.userRoles.domain.Role;
import com.proveritus.userservice.userManager.userRoles.domain.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserValidator userValidator;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private SignUpRequest signUpRequest;
    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("testuser");
        signUpRequest.setEmail("test@example.com");
        signUpRequest.setPassword("password");
        signUpRequest.setRoles(Set.of("VIEWER"));

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("testuser");
    }

    @Test
    void registerUser_ShouldReturnUserDTO_WhenSignUpRequestIsValid() {
        when(userRepository.existsByUsername(signUpRequest.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(false);
        when(userMapper.toDto(signUpRequest)).thenReturn(new UserDTO());
        when(userMapper.fromCreateDto(signUpRequest)).thenReturn(user);
        when(roleRepository.findByName("VIEWER")).thenReturn(Optional.of(new Role("VIEWER")));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDTO);
        doNothing().when(userValidator).validate(any(UserDTO.class), anyBoolean(), anyBoolean());

        UserDTO result = userService.registerUser(signUpRequest);

        assertEquals(userDTO, result);
        verify(userValidator).validate(any(UserDTO.class), anyBoolean(), anyBoolean());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void assignRolesToUser_ShouldReturnUserWithNewRoles() {
        when(userRepository.findOne(any(org.springframework.data.jpa.domain.Specification.class))).thenReturn(Optional.of(user));
        when(roleRepository.findByName("ADMIN")).thenReturn(Optional.of(new Role("ADMIN")));
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(userDTO);

        UserDTO result = userService.assignRolesToUser(1L, Set.of("ADMIN"));

        assertEquals(userDTO, result);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void assignRolesToUser_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findOne(any(org.springframework.data.jpa.domain.Specification.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            userService.assignRolesToUser(1L, Set.of("ADMIN"));
        });
    }
}