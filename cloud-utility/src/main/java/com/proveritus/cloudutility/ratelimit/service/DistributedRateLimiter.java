package com.proveritus.cloudutility.ratelimit.service;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.grid.GridBucketState;
import io.github.bucket4j.grid.ProxyManager;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;

@Service
public class DistributedRateLimiter implements RateLimitService {

    private final ProxyManager<String> proxyManager;

    public DistributedRateLimiter(ProxyManager<String> proxyManager) {
        this.proxyManager = proxyManager;
    }

    @Override
    public Bucket resolveBucket(String key) {
        Supplier<GridBucketState> stateSupplier = () -> {
            // define your bucket configuration here
            return new GridBucketState();
        };
        return Bucket4j.extension(io.github.bucket4j.grid.jcache.JCache.class).builder()
                .build(proxyManager, key, stateSupplier);
    }
}