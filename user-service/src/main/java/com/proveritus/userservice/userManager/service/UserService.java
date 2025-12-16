package com.proveritus.userservice.userManager.service;

import com.proveritus.cloudutility.dto.UserDTO;
import com.proveritus.cloudutility.jpa.DomainService;
import com.proveritus.cloudutility.passwordManager.dto.ResetPasswordRequest;
import com.proveritus.userservice.auth.dto.requests.SignUpRequest;
import com.proveritus.userservice.auth.domain.User;
import com.proveritus.userservice.userManager.dto.UpdateUserDTO;
import java.util.List;
import java.util.Optional;
import java.util.Set;


public interface UserService extends DomainService<User, SignUpRequest, UpdateUserDTO, UserDTO> {

    UserDTO registerUser(SignUpRequest signUpRequest);

    UserDTO getCurrentUser();

    Optional<UserDTO> getUserByUsername(String username);

    List<UserDTO> getUsersByIds(List<Long> ids);
    boolean isCurrentUser(Long id);

    UserDTO assignUserGroupsToUser(Long userId, Set<String> groupNames);

    UserDTO assignPermissionsToUser(Long userId, Set<String> permissionNames);

    UserDTO removePermissionsFromUser(Long userId, Set<String> permissionNames);

    void deactivateUser(Long id);

    void activateUser(Long id);

    void resetPassword(Long userId, ResetPasswordRequest request);

    long countAllUsers();

    User getUserEntityById(Long id);
}
