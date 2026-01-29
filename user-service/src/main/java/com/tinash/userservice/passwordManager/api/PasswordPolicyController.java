package com.tinash.userservice.passwordManager.api;

import com.tinash.userservice.passwordManager.service.PasswordPolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.tinash.cloud.utility.password.dto.PasswordPolicyDTO;

@RestController
@RequestMapping("/api/password-policy")
@RequiredArgsConstructor
public class PasswordPolicyController {

    private final PasswordPolicyService passwordPolicyService;

    @GetMapping
    @PreAuthorize("hasAuthority('VIEW_PASSWORD_POLICY')")
    public ResponseEntity<PasswordPolicyDTO> getPasswordPolicy() {
        return ResponseEntity.ok(passwordPolicyService.getPasswordPolicy());
    }

    @PutMapping
    @PreAuthorize("hasAuthority('UPDATE_PASSWORD_POLICY')")
    public ResponseEntity<PasswordPolicyDTO> updatePasswordPolicy(@RequestBody PasswordPolicyDTO passwordPolicyDTO) {
        return ResponseEntity.ok(passwordPolicyService.updatePasswordPolicy(passwordPolicyDTO));
    }
}
