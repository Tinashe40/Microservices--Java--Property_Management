package com.tinash.cloud.utility.ratelimit.strategy;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;

import java.time.Duration;

/**
 * Sliding window rate limiting strategy.
 * Uses a sliding time window that moves continuously rather than fixed time blocks.
 */
public class SlidingWindowStrategy {

    private final Bucket bucket;

    public SlidingWindowStrategy(long capacity, Duration duration) {
        // FIXED: Bucket4j doesn't have slidingWindow() method
        // Use greedy refill for sliding window behavior
        Refill refill = Refill.greedy(capacity, duration);
        Bandwidth limit = Bandwidth.classic(capacity, refill);
        this.bucket = Bucket.builder().addLimit(limit).build();
    }

    public boolean tryConsume() {
        return bucket.tryConsume(1);
    }

    public Bucket getBucket() {
        return bucket;
    }
}