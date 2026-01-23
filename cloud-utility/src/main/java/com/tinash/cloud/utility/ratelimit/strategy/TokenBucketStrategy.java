package com.tinash.cloud.utility.ratelimit.strategy;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;

import java.time.Duration;

public class TokenBucketStrategy {

    private final Bucket bucket;

    public TokenBucketStrategy(long capacity, long tokens, Duration duration) {
        Refill refill = Refill.intervally(tokens, duration);
        Bandwidth limit = Bandwidth.classic(capacity, refill);
        this.bucket = Bucket.builder().addLimit(limit).build();
    }

    public boolean tryConsume() {
        return bucket.tryConsume(1);
    }
}
