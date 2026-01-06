package com.proveritus.userservice.domain.repository;

import com.proveritus.cloudutility.core.port.out.Repository;
import com.proveritus.userservice.domain.model.user.Email;
import com.proveritus.userservice.domain.model.user.User;

import java.util.Optional;

/**
 * User repository interface (port).
 * Infrastructure layer implements this.
 */
public interface UserRepository extends Repository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(Email email);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(Email email);
}
