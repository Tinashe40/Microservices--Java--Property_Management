package com.tinash.cloud.utility.client;

import com.tinash.cloud.utility.dto.common.UserDto; // Assuming this will be the canonical DTO
import lombok.extern.slf4j.Slf4j; // Import Slf4j
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Feign Client for interacting with the 'user-service'.
 * Provides various API endpoints and includes a fallback mechanism for resilience.
 */
@FeignClient(name = "user-service", configuration = FeignConfig.class, fallback = UserClient.UserClientFallback.class)
public interface UserClient {

    @GetMapping("/api/users/me")
    Optional<UserDto> getCurrentUser(); // Changed to return Optional

    @GetMapping("/api/v1/users/{id}")
    Optional<UserDto> getUserById(@PathVariable("id") Long id);

    @GetMapping("/api/users")
    List<UserDto> getAllUsers(); // Changed to use UserDto

    @PutMapping("/api/users")
    UserDto updateUser(@RequestBody UserDto userDto); // Changed to use UserDto

    @PostMapping("/api/users/{userId}/expire-password")
    void expirePassword(@PathVariable("userId") Long userId);

    /**
     * Fallback implementation for UserClient.
     * This class provides default responses when the user-service is unavailable or
     * experiences an error, preventing cascade failures.
     */
    @Component
    @Slf4j // Add Slf4j annotation
    class UserClientFallback implements UserClient {

        @Override
        public Optional<UserDto> getCurrentUser() {
            log.error("Fallback activated for getCurrentUser: User service is down or experienced an error."); // Use log.error
            return Optional.empty();
        }

        @Override
        public Optional<UserDto> getUserById(Long id) {
            log.error("Fallback activated for getUserById: User service is down or experienced an error for ID: {}", id); // Use log.error
            return Optional.empty();
        }

        @Override
        public List<UserDto> getAllUsers() {
            log.error("Fallback activated for getAllUsers: User service is down or experienced an error."); // Use log.error
            return List.of(); // Return empty list on fallback
        }

        @Override
        public UserDto updateUser(UserDto userDto) {
            log.error("Fallback activated for updateUser: User service is down or experienced an error for user: {}", userDto.getId()); // Use log.error
            // Depending on the use case, you might return a default UserDto or throw a specific exception
            return new UserDto(); // Return a default/empty DTO
        }

        @Override
        public void expirePassword(Long userId) {
            log.error("Fallback activated for expirePassword: User service is down or experienced an error for user ID: {}", userId); // Use log.error
            // No return value, just log the error
        }
    }
}