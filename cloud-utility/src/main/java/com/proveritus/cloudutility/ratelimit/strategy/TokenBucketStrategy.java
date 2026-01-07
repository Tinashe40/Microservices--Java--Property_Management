package com.proveritus.cloudutility.ratelimit.strategy;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;

import java.time.Duration;

public class TokenBucketStrategy {

    private final Bucket bucket;

    public TokenBucketStrategy(long capacity, long tokens, Duration duration) {
        Refill refill = Refill.intervally(tokens, duration);
        Bandwidth limit = Bandwidth.classic(capacity, refill);
        this.bucket = Bucket4j.builder().addLimit(limit).build();
    }

    public boolean tryConsume() {
        return bucket.tryConsume(1);
    }
}