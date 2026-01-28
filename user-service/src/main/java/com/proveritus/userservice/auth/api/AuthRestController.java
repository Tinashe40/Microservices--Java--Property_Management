package com.proveritus.userservice.auth.api;

import com.proveritus.userservice.auth.dto.responses.JwtAuthResponse;
import com.proveritus.userservice.auth.dto.requests.LoginRequest;
import com.proveritus.userservice.auth.dto.requests.SignUpRequest;
import com.proveritus.userservice.auth.dto.responses.LoginResponse;
import com.proveritus.userservice.jwt.UserTokenService;
import com.proveritus.userservice.userManager.service.UserService;
import com.tinash.cloud.utility.dto.common.UserDto;
import com.tinash.cloud.utility.security.jwt.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "APIs for user authentication")
public class AuthRestController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider tokenProvider;
    private final UserTokenService userTokenService;

    @PostMapping("/sign-in")
    @Operation(summary = "Authenticates a user and returns a JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    public ResponseEntity<LoginResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        UserDto userDTO = userService.getUserByUsername(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + authentication.getName()));

        userTokenService.saveUserToken(jwt,
                userService.getUserEntityById(userDTO.getId()),
                tokenProvider.getExpirationDateFromToken(jwt)
                        .toInstant());

        return ResponseEntity.ok(new LoginResponse(new JwtAuthResponse(jwt), userDTO));
    }

    @PostMapping("/signup")
    @Operation(summary = "Registers a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid user data")
    })
    @Auditable
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        UserDto result = userService.registerUser(signUpRequest);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{id}")
                .buildAndExpand(result.getId()).toUri();

        return ResponseEntity.created(location).body(result);
    }

    @GetMapping("/me")
    @Operation(summary = "Gets the current user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Current user found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @Auditable
    public ResponseEntity<UserDto> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @PostMapping("/logout")
    @Operation(summary = "Logs out the current user by blacklisting their JWT token")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        try {
            SecurityUtils.getJwtFromRequest(request).ifPresent(userTokenService::blacklistUserToken);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during logout.");
        }
    }

    @PostMapping("/is-token-valid")
    @Operation(summary = "Checks if a JWT token is valid and exists in the database")
    public ResponseEntity<Boolean> isTokenValid(@RequestBody String token) {
        return ResponseEntity.ok(userTokenService.isTokenValid(token));
    }
}
