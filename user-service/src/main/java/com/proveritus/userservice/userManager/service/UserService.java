package com.proveritus.userservice.userManager.service;

import com.proveritus.cloudutility.dto.UserDTO;
import com.proveritus.cloudutility.jpa.DomainService;
import com.proveritus.cloudutility.jpa.Updatable;
import com.proveritus.userservice.Auth.DTO.SignUpRequest;
import com.proveritus.userservice.Auth.domain.User;
import com.proveritus.userservice.userManager.dto.UpdateUserDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service Interface for managing {@link User}.
 */
public interface UserService extends DomainService<User, SignUpRequest, UpdateUserDTO, UserDTO> {

    UserDTO registerUser(SignUpRequest signUpRequest);

    UserDTO getCurrentUser();

    Optional<UserDTO> getUserByUsername(String username);

    List<UserDTO> getUsersByIds(List<Long> ids);

    boolean isCurrentUser(Long id);

    UserDTO assignRolesToUser(Long userId, Set<String> roleNames);

    void deleteUser(Long id);
}