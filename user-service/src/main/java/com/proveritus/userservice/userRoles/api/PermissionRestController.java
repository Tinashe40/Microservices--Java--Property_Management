package com.proveritus.userservice.userRoles.api;

import com.proveritus.userservice.userRoles.dto.PermissionDTO;
import com.proveritus.userservice.userRoles.service.PermissionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@Tag(name = "Permissions", description = "APIs for permission management")
public class PermissionRestController {

    private final PermissionService permissionService;

    @GetMapping
    @PreAuthorize("hasAuthority('permission:read')")
    public ResponseEntity<Page<PermissionDTO>> getAllPermissions(Pageable pageable) {
        return ResponseEntity.ok(permissionService.findAll(pageable));
    }
}