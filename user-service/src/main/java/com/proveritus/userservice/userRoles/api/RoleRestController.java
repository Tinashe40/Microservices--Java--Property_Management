package com.proveritus.userservice.userRoles.api;

import com.proveritus.userservice.security.Roles;
import com.proveritus.userservice.userRoles.dto.RoleDTO;
import com.proveritus.userservice.userRoles.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@Tag(name = "Roles", description = "APIs for managing roles")
@PreAuthorize(Roles.HAS_ANY_ROLE_ADMIN_SUPER_ADMIN)
public class RoleRestController {

    private final RoleService roleService;

    @GetMapping
    @Operation(summary = "Get all roles")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<RoleDTO>> getAllRoles(Pageable pageable) {
        return ResponseEntity.ok(roleService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a role by ID")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<RoleDTO> getRoleById(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new role")
    public ResponseEntity<RoleDTO> createRole(@Valid @RequestBody RoleDTO createRoleDTO) {
        RoleDTO roleDTO = roleService.create(createRoleDTO);
        return new ResponseEntity<>(roleDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing role")
    public ResponseEntity<RoleDTO> updateRole(@PathVariable Long id, @Valid @RequestBody RoleDTO roleDTO) {
        roleDTO.setId(id);
        return ResponseEntity.ok(roleService.update(roleDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a role")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Role deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Role not found")
    })
    public ResponseEntity<Void> deleteRole(@Parameter(description = "ID of the role to delete") @PathVariable Long id) {
        roleService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
