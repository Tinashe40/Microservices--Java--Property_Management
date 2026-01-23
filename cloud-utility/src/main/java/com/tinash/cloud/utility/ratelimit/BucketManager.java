package com.tinash.cloud.utility.ratelimit;

import com.tinash.cloud.utility.ratelimit.service.RateLimitService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.grid.GridBucketState;
import jakarta.annotation.Nullable;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

/**
 * Manages rate limit buckets for different keys using Bucket4j.
 * This class provides a way to create and retrieve buckets dynamically based on
 * configured bandwidths, enabling flexible rate limiting strategies.
 * It supports both in-memory and distributed (via ProxyManager) bucket storage.
 */
@Component
public class BucketManager implements RateLimitService {

    private final ConcurrentMap<String, Bucket> inMemoryBuckets = new ConcurrentHashMap<>();
    private final ProxyManager<String> proxyManager;

    // Constructor for distributed setup (with JCache/Redis ProxyManager)
    public BucketManager(@Nullable ProxyManager<String> proxyManager) {
        this.proxyManager = proxyManager;
    }

    /**
     * Retrieves or creates a Bucket for a given key with the specified bandwidth configuration.
     * Prioritizes distributed buckets if a ProxyManager is available, otherwise uses in-memory.
     *
     * @param key The unique key for the bucket (e.g., IP address, user ID, API endpoint).
     * @param capacity The maximum number of tokens the bucket can hold.
     * @param refillTokens The number of tokens added to the bucket per refill period.
     * @param refillPeriod The duration of the refill period.
     * @return The Bucket instance for the given key.
     */
    @Override
    public Bucket resolveBucket(String key) {
        // Default values for capacity, refillTokens, refillPeriod.
        // These should ideally come from configuration or be passed from @RateLimited annotation.
        // For now, using reasonable defaults.
        long defaultCapacity = 100;
        long defaultRefillTokens = 100;
        Duration defaultRefillPeriod = Duration.ofMinutes(1);

        return resolveBucket(key, defaultCapacity, defaultRefillTokens, defaultRefillPeriod);
    }

    public Bucket resolveBucket(String key, long capacity, long refillTokens, Duration refillPeriod) {
        if (proxyManager != null) {
            // Distributed bucket using ProxyManager (e.g., JCache, Redis)
            Supplier<BucketConfiguration> configSupplier = () -> configureBucket(capacity, refillTokens, refillPeriod);
            return proxyManager.builder().build(key, configSupplier);
        } else {
            // In-memory fallback
            return inMemoryBuckets.computeIfAbsent(key, k -> createNewBucket(capacity, refillTokens, refillPeriod));
        }
    }

    private Bucket createNewBucket(long capacity, long refillTokens, Duration refillPeriod) {
        Refill refill = Refill.intervally(refillTokens, refillPeriod);
        Bandwidth limit = Bandwidth.classic(capacity, refill);
        return Bucket.builder().addLimit(limit).build();
    }

    private BucketConfiguration configureBucket(long capacity, long refillTokens, Duration refillPeriod) {
        Refill refill = Refill.intervally(refillTokens, refillPeriod);
        Bandwidth limit = Bandwidth.classic(capacity, refill);
        return BucketConfiguration.builder().addLimit(limit).build();
    }
}
