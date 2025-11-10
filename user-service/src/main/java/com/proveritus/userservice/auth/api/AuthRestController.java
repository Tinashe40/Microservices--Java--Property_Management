package com.proveritus.userservice.auth.api;

import com.proveritus.cloudutility.dto.UserDTO;
import com.proveritus.userservice.auth.dto.responses.JwtAuthResponse;
import com.proveritus.userservice.auth.dto.requests.LoginRequest;
import com.proveritus.userservice.auth.dto.requests.SignUpRequest;
import com.proveritus.cloudutility.security.JwtTokenProvider;
import com.proveritus.userservice.userManager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "APIs for user authentication")
public class AuthRestController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtTokenProvider tokenProvider;

    @PostMapping("/sign-in")
    @Operation(summary = "Authenticates a user and returns a JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authentication successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    public ResponseEntity<com.proveritus.userservice.auth.dto.responses.LoginResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);
        UserDTO userDTO = userService.getUserByUsername(authentication.getName()).orElseThrow();

        return ResponseEntity.ok(new com.proveritus.userservice.auth.dto.responses.LoginResponse(new JwtAuthResponse(jwt), userDTO));
    }

    @PostMapping("/signup")
    @Operation(summary = "Registers a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid user data")
    })
    public ResponseEntity<UserDTO> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        UserDTO result = userService.registerUser(signUpRequest);

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
    public ResponseEntity<UserDTO> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @GetMapping("/validate-token")
    @Operation(summary = "Validates a JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token is valid"),
            @ApiResponse(responseCode = "401", description = "Token is invalid")
    })
    public ResponseEntity<?> validateToken(@RequestParam String token) {
        if (tokenProvider.isTokenValid(token)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).build();
        }
    }

//    @GetMapping("/validate-token-and-get-roles")
//    @Operation(summary = "Validates a JWT token and returns user roles")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Token is valid"),
//            @ApiResponse(responseCode = "401", description = "Token is invalid")
//    })
//    public ResponseEntity<?> validateTokenAndGetRoles(@RequestParam String token) {
//        if (tokenProvider.isTokenValid(token)) {
//            Authentication authentication = tokenProvider.getAuthentication(token);
//            return ResponseEntity.ok(authentication.getAuthorities());
//        } else {
//            return ResponseEntity.status(org.springframework.http.HttpStatus.UNAUTHORIZED).build();
//        }
//    }
}