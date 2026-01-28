package com.proveritus.userservice.userGroups.api;

import com.proveritus.userservice.userGroups.dto.UserGroupDTO;
import com.proveritus.userservice.userGroups.service.UserGroupService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-groups")
@RequiredArgsConstructor
@Tag(name = "UserGroup", description = "APIs for user group management")
public class UserGroupRestController {

    private final UserGroupService userGroupService;

    @PostMapping
    @PreAuthorize("hasAuthority(T(com.proveritus.cloudutility.security.Permissions.UserGroup).CREATE)")
    public ResponseEntity<UserGroupDTO> createUserGroup(@Valid @RequestBody UserGroupDTO userGroupDTO) {
        return ResponseEntity.ok(userGroupService.create(userGroupDTO));
    }

    @GetMapping
    @PreAuthorize("hasAuthority(T(com.proveritus.cloudutility.security.Permissions.UserGroup).READ)")
    public ResponseEntity<Page<UserGroupDTO>> getAllUserGroups(Pageable pageable) {
        return ResponseEntity.ok(userGroupService.findAll(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.proveritus.cloudutility.security.Permissions.UserGroup).READ)")
    public ResponseEntity<UserGroupDTO> getUserGroupById(@PathVariable Long id) {
        return ResponseEntity.ok(userGroupService.findById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority(T(UserGroup).UPDATE)")
    public ResponseEntity<UserGroupDTO> updateUserGroup(@PathVariable Long id, @Valid @RequestBody UserGroupDTO userGroupDTO) {
        userGroupDTO.setId(id);
        return ResponseEntity.ok(userGroupService.update(userGroupDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.proveritus.cloudutility.security.Permissions.UserGroup).DELETE)")
    public ResponseEntity<Void> deleteUserGroup(@PathVariable Long id) {
        userGroupService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}