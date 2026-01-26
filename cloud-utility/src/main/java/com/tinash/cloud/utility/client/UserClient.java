package com.tinash.cloud.utility.client;

import com.tinash.cloud.utility.dto.common.UserDto; // Assuming this will be the canonical DTO
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
    class UserClientFallback implements UserClient {

        @Override
        public Optional<UserDto> getCurrentUser() {
            System.err.println("Fallback activated for getCurrentUser: User service is down or experienced an error.");
            return Optional.empty();
        }

        @Override
        public Optional<UserDto> getUserById(Long id) {
            System.err.println("Fallback activated for getUserById: User service is down or experienced an error for ID: " + id);
            return Optional.empty();
        }

        @Override
        public List<UserDto> getAllUsers() {
            System.err.println("Fallback activated for getAllUsers: User service is down or experienced an error.");
            return List.of(); // Return empty list on fallback
        }

        @Override
        public UserDto updateUser(UserDto userDto) {
            System.err.println("Fallback activated for updateUser: User service is down or experienced an error for user: " + userDto.getId());
            // Depending on the use case, you might return a default UserDto or throw a specific exception
            return new UserDto(); // Return a default/empty DTO
        }

        @Override
        public void expirePassword(Long userId) {
            System.err.println("Fallback activated for expirePassword: User service is down or experienced an error for user ID: " + userId);
            // No return value, just log the error
        }
    }
}