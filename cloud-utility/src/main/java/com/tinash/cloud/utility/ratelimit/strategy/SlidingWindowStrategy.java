package com.tinash.cloud.utility.ratelimit.strategy;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;

import java.time.Duration;

public class SlidingWindowStrategy {

    private final Bucket bucket;

    public SlidingWindowStrategy(long capacity, Duration duration) {
        Bandwidth limit = Bandwidth.slidingWindow(capacity, duration);
        this.bucket = Bucket.builder().addLimit(limit).build();
    }

    public boolean tryConsume() {
        return bucket.tryConsume(1);
    }
}
