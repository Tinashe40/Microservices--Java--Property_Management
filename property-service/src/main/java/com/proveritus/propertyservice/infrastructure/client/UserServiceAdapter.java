package com.proveritus.propertyservice.infrastructure.client;

import com.proveritus.cloudutility.dto.UserDTO;
import com.proveritus.propertyservice.application.port.out.UserServicePort;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adapter for user service communication.
 * Implements UserServicePort interface.
 * Follows Dependency Inversion Principle.
 */
@Slf4j 
@Component 
@RequiredArgsConstructor
public class UserServiceAdapter implements UserServicePort {

    private final UserServiceClient client;

    @Override 
    @CircuitBreaker(name = "user-service", fallbackMethod = "findUserByIdFallback")
    @Retry(name = "user-service")
    public Optional<UserDTO> findUserById(String userId) {
        try {
            return Optional.ofNullable(client.getUserById(userId));
        } catch (Exception e) {
            log.warn("Failed to fetch user {}: {}", userId, e.getMessage());
            return Optional.empty();
        }
    }

    @Override 
    @CircuitBreaker(name = "user-service", fallbackMethod = "findUsersByIdsFallback")
    @Retry(name = "user-service")
    public List<UserDTO> findUsersByIds(List<String> userIds) {
        try {
            return client.getUsersByIds(userIds);
        } catch (Exception e) {
            log.warn("Failed to fetch users: {}", e.getMessage());
            return List.of();
        }
    }

    // Fallback methods
    private Optional<UserDTO> findUserByIdFallback(String userId, Exception e) {
        log.warn("Fallback for user {}: {}", userId, e.getMessage());
        return Optional.empty();
    }

    private List<UserDTO> findUsersByIdsFallback(List<String> userIds, Exception e) {
        log.warn("Fallback for users: {}", e.getMessage());
        return List.of();
    }
}
