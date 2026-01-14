package com.proveritus.cloudutility.ratelimit.strategy;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;

import java.time.Duration;

public class FixedWindowStrategy {

    private final Bucket bucket;

    public FixedWindowStrategy(long capacity, Duration duration) {
        Refill refill = Refill.intervally(capacity, duration);
        Bandwidth limit = Bandwidth.classic(capacity, refill);
        this.bucket = io.github.bucket4j.Bucket4j.builder().addLimit(limit).build();
    }

    public boolean tryConsume() {
        return bucket.tryConsume(1);
    }
}