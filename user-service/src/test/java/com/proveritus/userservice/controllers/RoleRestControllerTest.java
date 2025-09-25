package com.proveritus.userservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proveritus.cloudutility.security.JwtAccessDeniedHandler;
import com.proveritus.cloudutility.security.JwtAuthenticationEntryPoint;
import com.proveritus.cloudutility.security.JwtTokenProvider;
import com.proveritus.userservice.config.SecurityConfig;
import com.proveritus.userservice.userManager.userRoles.api.RoleRestController;
import com.proveritus.userservice.userManager.userRoles.dto.CreateRoleDTO;
import com.proveritus.userservice.userManager.userRoles.dto.RoleDTO;
import com.proveritus.userservice.userManager.userRoles.dto.UpdateRoleDTO;
import com.proveritus.userservice.userManager.userRoles.service.RoleService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RoleRestController.class)
@Import(SecurityConfig.class)
public class RoleRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoleService roleService;

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
    void getAllRoles_ShouldReturnPagedRoles_WhenUserIsAdmin() throws Exception {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(1L);
        roleDTO.setName("ADMIN");
        Page<RoleDTO> rolePage = new PageImpl<>(List.of(roleDTO), PageRequest.of(0, 20), 1);

        when(roleService.findAll(any(PageRequest.class))).thenReturn(rolePage);

        mockMvc.perform(get("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].name").value("ADMIN"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getRoleById_ShouldReturnRole_WhenRoleExists() throws Exception {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(1L);
        roleDTO.setName("ADMIN");

        when(roleService.findById(1L)).thenReturn(roleDTO);

        mockMvc.perform(get("/api/roles/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("ADMIN"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createRole_ShouldReturnCreatedRole_WhenUserIsAdmin() throws Exception {
        CreateRoleDTO createRoleDTO = new CreateRoleDTO();
        createRoleDTO.setName("NEW_ROLE");

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(1L);
        roleDTO.setName("NEW_ROLE");

        when(roleService.create(any(CreateRoleDTO.class))).thenReturn(roleDTO);

        mockMvc.perform(post("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRoleDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("NEW_ROLE"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateRole_ShouldReturnUpdatedRole_WhenUserIsAdmin() throws Exception {
        UpdateRoleDTO updateRoleDTO = new UpdateRoleDTO();
        updateRoleDTO.setName("UPDATED_ROLE");

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setId(1L);
        roleDTO.setName("UPDATED_ROLE");

        when(roleService.update(any(UpdateRoleDTO.class))).thenReturn(roleDTO);

        mockMvc.perform(put("/api/roles/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRoleDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("UPDATED_ROLE"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteRole_ShouldReturnNoContent_WhenUserIsAdmin() throws Exception {
        doNothing().when(roleService).deleteById(1L);

        mockMvc.perform(delete("/api/roles/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}