package com.proveritus.propertyservice.application.port.out;

import com.proveritus.cloudutility.dto.UserDTO;

import java.util.List;
import java.util.Optional;

/**
 * Port for user service communication.
 * Follows Dependency Inversion Principle.
 */
public interface UserServicePort {
    Optional<UserDTO> findUserById(String userId);
    List<UserDTO> findUsersByIds(List<String> userIds);
}
