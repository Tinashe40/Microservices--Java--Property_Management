package com.tinash.cloud.utility.ratelimit;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Manages rate limit buckets for different keys using Bucket4j.
 * This class provides a way to create and retrieve buckets dynamically based on
 * configured bandwidths, enabling flexible rate limiting strategies.
 *
 * It uses a simple in-memory map for bucket storage. For distributed
 * environments, a distributed ProxyManager (e.g., using Redis or JCache)
 * should be injected instead of relying on a ConcurrentHashMap directly.
 */
@Component
public class BucketManager {

    // In a distributed setup, this would be a ProxyManager for JCache, Redis, etc.
    // For now, using a simple in-memory map for demonstration.
    private final ConcurrentMap<String, Bucket> buckets = new ConcurrentHashMap<>();

    // If using a distributed cache like JCache, you'd inject a ProxyManager here:
    // private final ProxyManager<String> proxyManager;
    // public BucketManager(ProxyManager<String> proxyManager) { this.proxyManager = proxyManager; }

    /**
     * Retrieves or creates a Bucket for a given key with the specified bandwidth configuration.
     *
     * @param key The unique key for the bucket (e.g., IP address, user ID, API endpoint).
     * @param capacity The maximum number of tokens the bucket can hold.
     * @param refillTokens The number of tokens added to the bucket per refill period.
     * @param refillPeriod The duration of the refill period.
     * @return The Bucket instance for the given key.
     */
    public Bucket getBucket(String key, long capacity, long refillTokens, Duration refillPeriod) {
        // In a production setup with distributed caching, you would use proxyManager here:
        // return proxyManager.get      (key, () -> configureBucket(capacity, refillTokens, refillPeriod));
        // The configureBucket method would return BucketConfiguration.
        return buckets.computeIfAbsent(key, k -> createNewBucket(capacity, refillTokens, refillPeriod));
    }

    private Bucket createNewBucket(long capacity, long refillTokens, Duration refillPeriod) {
        Refill refill = Refill.intervally(refillTokens, refillPeriod);
        Bandwidth limit = Bandwidth.classic(capacity, refill);
        return Bucket.builder().addLimit(limit).build();
    }

    // If using a ProxyManager with JCache:
    // private BucketConfiguration configureBucket(long capacity, long refillTokens, Duration refillPeriod) {
    //     Refill refill = Refill.intervally(refillTokens, refillPeriod);
    //     Bandwidth limit = Bandwidth.classic(capacity, refill);
    //     return BucketConfiguration.builder().addLimit(limit).build();
    // }
}
