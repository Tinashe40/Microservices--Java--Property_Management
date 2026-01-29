package com.tinash.userservice.jwt;

import com.tinash.cloud.utility.jpa.BaseRepository;
import com.tinash.userservice.domain.model.user.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTokenRepository extends BaseRepository<UserToken, String> {

    Optional<UserToken> findByToken(String token);
}