package com.proveritus.cloudutility.security.ratelimit;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

/**
 * Service for rate limiting operations.
 */
@Slf4j
@Service 
@RequiredArgsConstructor
public class RateLimitService {

    private final ProxyManager<String> proxyManager;

    /**
     * Resolves or creates a bucket for the given key.
     *
     * @param key         the unique key (e.g., IP, user ID)
     * @param configSupplier supplier for bucket configuration
     * @return the bucket
     */
    public Bucket resolveBucket(String key, Supplier<BucketConfiguration> configSupplier) {
        return proxyManager.builder()
                .build(key, configSupplier);
    }

    /**
     * Checks if a request is allowed for the given key.
     *
     * @param key         the unique key
     * @param configSupplier bucket configuration supplier
     * @return true if allowed, false if rate limit exceeded
     */
    public boolean allowRequest(String key, Supplier<BucketConfiguration> configSupplier) {
        Bucket bucket = resolveBucket(key, configSupplier);
        boolean allowed = bucket.tryConsume(1);
        
        if (!allowed) {
            log.warn("Rate limit exceeded for key: {}", key);
        }
        
        return allowed;
    }

    /**
     * Gets remaining tokens for a key.
     */
    public long getRemainingTokens(String key, Supplier<BucketConfiguration> configSupplier) {
        Bucket bucket = resolveBucket(key, configSupplier);
        return bucket.getAvailableTokens();
    }
}