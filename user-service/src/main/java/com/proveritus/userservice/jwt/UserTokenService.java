package com.proveritus.userservice.jwt;

import com.proveritus.userservice.domain.model.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserTokenService {

    private final UserTokenRepository userTokenRepository;

    @Transactional
    public void saveUserToken(String token, User user, Instant expiryDate) {
        UserToken userToken = new UserToken();
        userToken.setToken(token);
        userToken.setUser(user);
        userToken.setExpiryDate(expiryDate);
        userTokenRepository.save(userToken);
    }

    @Transactional
    public void blacklistUserToken(String token) {
        userTokenRepository.findByToken(token).ifPresent(userToken -> {
            userToken.setBlacklisted(true);
            userTokenRepository.save(userToken);
        });
    }

    public boolean isTokenValid(String token) {
        Optional<UserToken> userToken = userTokenRepository.findByToken(token);
        return userToken.isPresent() && !userToken.get().isBlacklisted();
    }
}
