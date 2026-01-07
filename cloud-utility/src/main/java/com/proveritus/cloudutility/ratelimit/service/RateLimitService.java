package com.proveritus.cloudutility.ratelimit.service;

import io.github.bucket4j.Bucket;

public interface RateLimitService {

    Bucket resolveBucket(String key);
}