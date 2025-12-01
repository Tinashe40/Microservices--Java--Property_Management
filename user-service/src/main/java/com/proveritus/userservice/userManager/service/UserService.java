package com.proveritus.userservice.userManager.service;

import com.proveritus.cloudutility.dto.UserDTO;
import com.proveritus.cloudutility.jpa.DomainService;
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

    UserDTO assignRolesToUser(Long userId, Set<String> roleNames);

    void deleteUser(Long id);

    void deactivateUser(Long id);

    void activateUser(Long id);

    void changePassword(com.proveritus.userservice.userManager.dto.ChangePasswordRequest request);

    void resetPassword(Long userId, com.proveritus.userservice.userManager.dto.ResetPasswordRequest request);

    long countAllUsers();
}
