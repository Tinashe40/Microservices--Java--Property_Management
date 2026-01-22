package com.proveritus.userservice.userGroups.api;

import com.tinash.cloud.utility.security.Permissions;
import com.proveritus.userservice.userGroups.dto.PermissionDTO;
import com.proveritus.userservice.userGroups.service.PermissionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
@Tag(name = "Permissions", description = "APIs for permission management")
public class PermissionRestController {

    private final PermissionService permissionService;

    @GetMapping
    @PreAuthorize("hasAuthority(T(com.proveritus.cloudutility.security.Permissions.Permission).READ)")
    public ResponseEntity<Page<PermissionDTO>> getAllPermissions(Pageable pageable) {
        return ResponseEntity.ok(permissionService.findAll(pageable));
    }
    @PostMapping
    @PreAuthorize("hasAuthority(T(com.proveritus.cloudutility.security.Permissions.Permission).CREATE)")
    public ResponseEntity<PermissionDTO> createPermission(@Valid @RequestBody PermissionDTO permissionDTO) {
        return ResponseEntity.ok(permissionService.create(permissionDTO));
    }
}