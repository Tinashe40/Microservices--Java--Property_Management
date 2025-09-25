package com.proveritus.userservice.userManager.domain;


import com.proveritus.cloudutility.jpa.BaseDao;
import com.proveritus.userservice.Auth.domain.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseDao<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
