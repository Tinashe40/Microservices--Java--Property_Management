package com.proveritus.userservice.userManager.api;

import com.tinash.cloud.utility.dto.common.UserDto;
import com.tinash.cloud.utility.exception.technical.ResourceNotFoundException;
import com.proveritus.userservice.passwordManager.password.dto.ResetPasswordRequest;
import com.proveritus.userservice.auth.dto.requests.SignUpRequest;
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
    @PreAuthorize("hasAuthority(T(com.proveritus.cloudutility.security.Permissions.User).READ)")
    @Operation(summary = "Get all users")
    public ResponseEntity<Page<UserDto>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.findAll(pageable));
    }

    @GetMapping("/count")
    @PreAuthorize("hasAuthority(T(com.proveritus.cloudutility.security.Permissions.User).READ)")
    @Operation(summary = "Get total number of users")
    public ResponseEntity<Long> getUsersCount() {
        return ResponseEntity.ok(userService.countAllUsers());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.proveritus.cloudutility.security.Permissions.User).READ) or @userServiceImpl.isCurrentUser(#id)")
    @Operation(summary = "Get user by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserDto> getUserById(@Parameter(description = "ID of the user to retrieve") @PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping("/by-username")
    @Operation(summary = "Get user by username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserDto> getUserByUsername(@Parameter(description = "Username of the user to retrieve") @RequestParam String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username)));
    }

    @PostMapping("/by-ids")
    @Operation(summary = "Get users by IDs")
    @PreAuthorize("hasAuthority(T(com.proveritus.cloudutility.security.Permissions.User).READ)")
    public ResponseEntity<List<UserDto>> getUsersByIds(@RequestBody List<Long> ids) {
        return ResponseEntity.ok(userService.getUsersByIds(ids));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.proveritus.cloudutility.security.Permissions.User).DELETE)")
    @Operation(summary = "Delete user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Void> deleteUserById(@Parameter(description = "ID of the user to delete") @PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    @PreAuthorize("hasAuthority(T(com.proveritus.cloudutility.security.Permissions.User).CREATE)")
    @Operation(summary = "Create a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid user data")
    })
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok(userService.registerUser(signUpRequest));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.proveritus.cloudutility.security.Permissions.User).UPDATE) or @userServiceImpl.isCurrentUser(#id)")
    @Operation(summary = "Update a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Invalid user data")
    })
    public ResponseEntity<UserDto> updateUser(@Parameter(description = "ID of the user to update") @PathVariable Long id, @Valid @RequestBody UpdateUserDto userDTO) {
        userDTO.setId(id);
        return ResponseEntity.ok(userService.update(userDTO));
    }

    @PreAuthorize("hasAuthority(T(com.proveritus.cloudutility.security.Permissions.User).ASSIGN_GROUPS)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Groups assigned successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/{id}/groups")
    public ResponseEntity<UserDto> assignGroupsToUser(@Parameter(description = "ID of the user to assign groups to")
                                                         @PathVariable Long id,
                                                     @RequestBody Set<String> groups) {
        return ResponseEntity.ok(userService.assignUserGroupsToUser(id, groups));
    }

    @PreAuthorize("hasAuthority(T(com.proveritus.cloudutility.security.Permissions.User).ASSIGN_PERMISSIONS)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permissions assigned successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/{id}/permissions")
    public ResponseEntity<UserDto> assignPermissionsToUser(@Parameter(description = "ID of the user to assign permissions to")
                                                            @PathVariable Long id,
                                                        @RequestBody Set<String> permissions) {
        return ResponseEntity.ok(userService.assignPermissionsToUser(id, permissions));
    }

    @PreAuthorize("hasAuthority(T(com.proveritus.cloudutility.security.Permissions.User).ASSIGN_PERMISSIONS)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Permissions removed successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}/permissions")
    public ResponseEntity<UserDto> removePermissionsFromUser(@Parameter(description = "ID of the user to remove permissions from")
                                                           @PathVariable Long id,
                                                       @RequestBody Set<String> permissions) {
        return ResponseEntity.ok(userService.removePermissionsFromUser(id, permissions));
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority(T(com.proveritus.cloudutility.security.Permissions.User).DEACTIVATE)")
    @Operation(summary = "Deactivate a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deactivated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Void> deactivateUser(@Parameter(description = "ID of the user to deactivate") @PathVariable Long id) {
        userService.deactivateUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasAuthority(T(com.proveritus.cloudutility.security.Permissions.User).UPDATE)")
    @Operation(summary = "Activate a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User activated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Void> activateUser(@Parameter(description = "ID of the user to activate") @PathVariable Long id) {
        userService.activateUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/reset-password")
    @PreAuthorize("hasAuthority(T(com.proveritus.cloudutility.security.Permissions.User).RESET_PASSWORD)")
    @Operation(summary = "Reset user password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password reset successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<Void> resetPassword(@Parameter(description = "ID of the user to reset password") @PathVariable Long id, @Valid @RequestBody ResetPasswordRequest request) {
        userService.resetPassword(id, request);
        return ResponseEntity.noContent().build();
    }
}
