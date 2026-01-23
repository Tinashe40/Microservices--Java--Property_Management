package com.tinash.cloud.utility.ratelimit.storage;

import io.github.bucket4j.Bucket;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryRateLimitStorage implements RateLimitStorage {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    @Override
    public Bucket getBucket(String key) {
        return buckets.get(key);
    }

    @Override
    public void saveBucket(String key, Bucket bucket) {
        buckets.put(key, bucket);
    }
}
