package com.tinash.cloud.utility.security.jwt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class JwtTokenBlacklist {

    private final Map<String, Long> blacklistedTokens = new ConcurrentHashMap<>();
    private static final long CLEANUP_INTERVAL_MS = 3600000; // 1 hour
    private long lastCleanup = System.currentTimeMillis();

    public void blacklist(String token, long ttlMs) {
        blacklistedTokens.put(token, System.currentTimeMillis() + ttlMs);
        performCleanup();
        log.debug("Token blacklisted, total blacklisted tokens: {}", blacklistedTokens.size());
    }

    public void blacklist(String token) {
        blacklist(token, 86400000);
    }

    public boolean isBlacklisted(String token) {
        Long expiry = blacklistedTokens.get(token);

        if (expiry == null) {
            return false;
        }

        if (expiry < System.currentTimeMillis()) {
            blacklistedTokens.remove(token);
            return false;
        }

        return true;
    }

    private synchronized void performCleanup() {
        long now = System.currentTimeMillis();
        if (now - lastCleanup < CLEANUP_INTERVAL_MS) {
            return;
        }

        int initialSize = blacklistedTokens.size();
        blacklistedTokens.entrySet().removeIf(entry -> entry.getValue() < now);
        lastCleanup = now;

        log.debug("Token blacklist cleaned up. Removed {} expired tokens.",
                initialSize - blacklistedTokens.size());
    }
}
