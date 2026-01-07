package com.proveritus.cloudutility.ratelimit.storage;

import io.github.bucket4j.Bucket;

public interface RateLimitStorage {

    Bucket getBucket(String key);

    void saveBucket(String key, Bucket bucket);
}