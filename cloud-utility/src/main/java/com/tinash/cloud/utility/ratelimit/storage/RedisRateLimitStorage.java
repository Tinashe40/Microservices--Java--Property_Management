package com.tinash.cloud.utility.ratelimit.storage;

import io.github.bucket4j.Bucket;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisRateLimitStorage implements RateLimitStorage {

    private final RedisTemplate<String, Bucket> redisTemplate;

    public RedisRateLimitStorage(RedisTemplate<String, Bucket> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Bucket getBucket(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void saveBucket(String key, Bucket bucket) {
        redisTemplate.opsForValue().set(key, bucket);
    }
}
