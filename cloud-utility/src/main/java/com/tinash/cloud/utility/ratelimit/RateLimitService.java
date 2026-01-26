
package com.tinash.cloud.utility.ratelimit;

import io.github.bucket4j.Bucket;

public interface RateLimitService {
    Bucket resolveBucket(String key);
}
