package com.proveritus.userservice.jwt;

import com.proveritus.cloudutility.security.JwtTokenProvider;
import com.proveritus.cloudutility.security.TokenBlacklist;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService implements TokenBlacklist {

    private final BlacklistedTokenRepository blacklistedTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public void blacklistToken(String token) {

        if (!jwtTokenProvider.isTokenValid(token)) {
            return;
        }
        Instant expiryDate = jwtTokenProvider.getExpirationDateFromToken(token).toInstant();
        BlacklistedToken blacklistedToken = new BlacklistedToken(token, expiryDate);
        blacklistedTokenRepository.save(blacklistedToken);
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklistedTokenRepository.existsById(token);
    }
}
