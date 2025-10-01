package com.proveritus.userservice.userRoles.api;

import com.proveritus.userservice.security.Roles;
import com.proveritus.userservice.userRoles.dto.PermissionDTO;
import com.proveritus.userservice.userRoles.service.PermissionService;
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
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
@Tag(name = "Permissions", description = "APIs for managing permissions")
@PreAuthorize(Roles.HAS_ANY_ROLE_ADMIN_SUPER_ADMIN)
public class PermissionRestController {

    private final PermissionService permissionService;

    @GetMapping
    @Operation(summary = "Get all permissions")
    public ResponseEntity<Page<PermissionDTO>> getAllPermissions(Pageable pageable) {
        return ResponseEntity.ok(permissionService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a permission by ID")
    public ResponseEntity<PermissionDTO> getPermissionById(@PathVariable Long id) {
        return ResponseEntity.ok(permissionService.findById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new permission")
    public ResponseEntity<PermissionDTO> createPermission(@Valid @RequestBody PermissionDTO createPermissionDTO) {
        PermissionDTO permissionDTO = permissionService.create(createPermissionDTO);
        return new ResponseEntity<>(permissionDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing permission")
    public ResponseEntity<PermissionDTO> updatePermission(@PathVariable Long id, @Valid @RequestBody PermissionDTO permissionDTO) {
        permissionDTO.setId(id);
        return ResponseEntity.ok(permissionService.update(permissionDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a permission")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Permission deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Permission not found")
    })
    public ResponseEntity<Void> deletePermission(@Parameter(description = "ID of the permission to delete") @PathVariable Long id) {
        permissionService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
