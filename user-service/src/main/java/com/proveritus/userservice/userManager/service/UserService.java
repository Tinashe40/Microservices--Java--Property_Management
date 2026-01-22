package com.proveritus.userservice.userManager.service;

import com.proveritus.userservice.auth.dto.requests.SignUpRequest;
import com.proveritus.userservice.auth.domain.User;
import java.util.List;
import java.util.Optional;
import java.util.Set;


public interface UserService extends DomainService<User, SignUpRequest, UpdateUserDto, UserDto> {

    UserDto registerUser(SignUpRequest signUpRequest);

    UserDto getCurrentUser();

    Optional<UserDto> getUserByUsername(String username);

    List<UserDto> getUsersByIds(List<Long> ids);
    boolean isCurrentUser(Long id);

    UserDto assignUserGroupsToUser(Long userId, Set<String> groupNames);

    UserDto assignPermissionsToUser(Long userId, Set<String> permissionNames);

    UserDto removePermissionsFromUser(Long userId, Set<String> permissionNames);

    void deactivateUser(Long id);

    void activateUser(Long id);

    void resetPassword(Long userId, ResetPasswordRequest request);

    long countAllUsers();

    User getUserEntityById(Long id);
}
