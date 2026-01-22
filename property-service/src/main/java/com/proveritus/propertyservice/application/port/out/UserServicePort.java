package com.proveritus.propertyservice.application.port.out;

import com.tinash.cloud.utility.dto.UserDto;

import java.util.List;
import java.util.Optional;

/**
 * Port for user service communication.
 * Follows Dependency Inversion Principle.
 */
public interface UserServicePort {
    Optional<UserDto> findUserById(String userId);
    List<UserDto> findUsersByIds(List<String> userIds);
}
