package com.proveritus.userservice.domain.repository;

import com.proveritus.userservice.domain.model.user.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, String> {

    Optional<UserToken> findByToken(String token);
}