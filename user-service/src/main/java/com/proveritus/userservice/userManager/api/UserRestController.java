package com.proveritus.userservice.userManager.api;

import com.proveritus.cloudutility.dto.UserDTO;
import com.proveritus.userservice.Auth.DTO.SignUpRequest;
import com.proveritus.userservice.userManager.dto.*;
import com.proveritus.userservice.userManager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "APIs for user management")
@SecurityRequirement(name = "bearerAuth")
public class UserRestController {

    private final UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Get all users (Admin only)")
    public ResponseEntity<Page<UserDTO>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or @userServiceImpl.isCurrentUser(#id)")
    @Operation(summary = "Get user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserDTO> getUserById(@Parameter(description = "ID of the user to retrieve") @PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("/by-username")
    @Operation(summary = "Get user by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserDTO> getUserByUsername(@Parameter(description = "Username of the user to retrieve") @RequestParam String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username)
                .orElseThrow(() -> new com.proveritus.cloudutility.exception.UserNotFoundException("User not found with username: " + username)));
    }

    @PostMapping("/by-ids")
    @Operation(summary = "Get users by IDs")
    public ResponseEntity<List<UserDTO>> getUsersByIds(@RequestBody List<Long> ids) {
        return ResponseEntity.ok(userService.getUsersByIds(ids));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Delete user (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Void> deleteUser(@Parameter(description = "ID of the user to delete") @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Create a new user (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid user data")
    })
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok(userService.registerUser(signUpRequest));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or @userServiceImpl.isCurrentUser(#id)")
    @Operation(summary = "Update a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Invalid user data")
    })
    public ResponseEntity<UserDTO> updateUser(@Parameter(description = "ID of the user to update") @PathVariable Long id, @Valid @RequestBody UpdateUserDTO userDTO) {
        userDTO.setId(id);
        return ResponseEntity.ok(userService.update(userDTO));
    }

    @PostMapping("/{id}/roles")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @Operation(summary = "Assign roles to a user (Admin only)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Roles assigned successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserDTO> assignRolesToUser(@Parameter(description = "ID of the user to assign roles to") @PathVariable Long id, @RequestBody Set<String> roles) {
        return ResponseEntity.ok(userService.assignRolesToUser(id, roles));
    }
}
