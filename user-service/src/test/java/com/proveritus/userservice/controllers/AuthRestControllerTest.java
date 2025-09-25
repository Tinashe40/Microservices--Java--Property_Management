package com.proveritus.userservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proveritus.cloudutility.dto.UserDTO;
import com.proveritus.cloudutility.security.JwtAccessDeniedHandler;
import com.proveritus.cloudutility.security.JwtAuthenticationEntryPoint;
import com.proveritus.userservice.Auth.DTO.LoginRequest;
import com.proveritus.userservice.Auth.DTO.SignUpRequest;
import com.proveritus.cloudutility.security.JwtTokenProvider;
import com.proveritus.userservice.Auth.api.AuthRestController;
import com.proveritus.userservice.config.SecurityConfig;
import com.proveritus.userservice.userManager.userRoles.domain.RoleRepository;
import com.proveritus.userservice.userManager.domain.UserRepository;
import com.proveritus.userservice.userManager.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthRestController.class)
@Import(SecurityConfig.class)
class AuthRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenProvider tokenProvider;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @MockBean
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void authenticateUser_ShouldReturnJwtToken_WhenCredentialsAreValid() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsernameOrEmail("testuser");
        loginRequest.setPassword("password");

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);

        String jwt = "test-jwt-token";
        when(tokenProvider.generateToken(authentication)).thenReturn(jwt);

        mockMvc.perform(post("/api/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value(jwt))
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }

    @Test
    void registerUser_ShouldCreateUserAndReturnUserDTO_WhenRequestIsValid() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("newuser");
        signUpRequest.setEmail("newuser@example.com");
        signUpRequest.setPassword("password123");
        signUpRequest.setFirstName("New");
        signUpRequest.setLastName("User");
        signUpRequest.setRoles(Collections.singleton("VIEWER"));

        UserDTO createdUser = new UserDTO();
        createdUser.setId(1L);
        createdUser.setUsername("newuser");
        createdUser.setEmail("newuser@example.com");

        when(userService.registerUser(any(SignUpRequest.class))).thenReturn(createdUser);

        mockMvc.perform(post("/api/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("newuser"));
    }
}