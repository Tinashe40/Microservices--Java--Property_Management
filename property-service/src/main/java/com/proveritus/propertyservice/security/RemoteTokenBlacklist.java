package com.proveritus.propertyservice.security;

import com.proveritus.cloudutility.security.TokenBlacklist;
import com.proveritus.propertyservice.client.UserAuthClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RemoteTokenBlacklist implements TokenBlacklist {

    private final UserAuthClient userAuthClient;

    @Override
    public boolean isTokenBlacklisted(String token) {
        try {
            ResponseEntity<Boolean> response = userAuthClient.isTokenBlacklisted(token);
            return response.getBody() != null && response.getBody();
        } catch (Exception e) {
            // Log the exception and assume the token is not blacklisted in case of an error.
            // This is a fallback to prevent locking out users if the user-service is down.
            return false;
        }
    }
}
