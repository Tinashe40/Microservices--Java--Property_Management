package com.proveritus.userservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proveritus.cloudutility.dto.UserDTO;
import com.proveritus.cloudutility.security.JwtAccessDeniedHandler;
import com.proveritus.cloudutility.security.JwtAuthenticationEntryPoint;
import com.proveritus.cloudutility.security.JwtTokenProvider;
import com.proveritus.userservice.Auth.DTO.SignUpRequest;
import com.proveritus.userservice.config.SecurityConfig;
import com.proveritus.userservice.userManager.api.UserController;
import com.proveritus.userservice.userManager.dto.UpdateUserDTO;
import com.proveritus.userservice.userManager.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @MockBean
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void getUserById_ShouldReturnUser_WhenUserExists() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("testuser");

        when(userService.findById(1L)).thenReturn(userDTO);

        mockMvc.perform(get("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllUsers_ShouldReturnPagedUsers_WhenUserIsAdmin() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("testuser");
        Page<UserDTO> userPage = new PageImpl<>(List.of(userDTO), PageRequest.of(0, 20), 1);

        when(userService.findAll(any(PageRequest.class))).thenReturn(userPage);

        mockMvc.perform(get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].username").value("testuser"));
    }

    @Test
    @WithMockUser
    void getUserByUsername_ShouldReturnUser_WhenUserExists() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("testuser");

        when(userService.getUserByUsername("testuser")).thenReturn(Optional.of(userDTO));

        mockMvc.perform(get("/api/users/by-username")
                        .param("username", "testuser")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    @WithMockUser
    void getUsersByIds_ShouldReturnUsers_WhenUsersExist() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("testuser");

        when(userService.getUsersByIds(List.of(1L))).thenReturn(List.of(userDTO));

        mockMvc.perform(post("/api/users/by-ids")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(1L))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].username").value("testuser"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteUser_ShouldReturnNoContent_WhenUserIsAdmin() throws Exception {
        doNothing().when(userService).deleteUser(1L);

        mockMvc.perform(delete("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createUser_ShouldReturnCreatedUser_WhenUserIsAdmin() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("newuser");
        signUpRequest.setEmail("newuser@example.com");
        signUpRequest.setPassword("password");
        signUpRequest.setFirstName("New");
        signUpRequest.setLastName("User");

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setUsername("newuser");

        when(userService.registerUser(any(SignUpRequest.class))).thenReturn(userDTO);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("newuser"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateUser_ShouldReturnUpdatedUser_WhenUserIsAdmin() throws Exception {
        UpdateUserDTO updateUserDTO = new UpdateUserDTO();
        updateUserDTO.setFirstName("Updated");
        updateUserDTO.setLastName("User");

        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setFirstName("Updated");
        userDTO.setLastName("User");

        when(userService.update(any(UpdateUserDTO.class))).thenReturn(userDTO);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("Updated"))
                .andExpect(jsonPath("$.lastName").value("User"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void assignRolesToUser_ShouldReturnUserWithNewRoles_WhenUserIsAdmin() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setRoles(Set.of("ADMIN"));

        when(userService.assignRolesToUser(1L, Set.of("ADMIN"))).thenReturn(userDTO);

        mockMvc.perform(post("/api/users/1/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Set.of("ADMIN"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.roles[0]").value("ADMIN"));
    }
}