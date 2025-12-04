package com.proveritus.userservice.jwt;

import com.proveritus.userservice.jwt.TokenBlacklistService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class LogoutController {

    private final TokenBlacklistService tokenBlacklistService;

    @PostMapping("/logout")
    @Operation(summary = "Logs out the current user by blacklisting their JWT token")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String token = getJwtFromRequest(request);
        if (StringUtils.hasText(token)) {
            tokenBlacklistService.blacklistToken(token);
        }
        return ResponseEntity.ok().build();
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
