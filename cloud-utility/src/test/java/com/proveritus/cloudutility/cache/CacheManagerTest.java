package com.proveritus.cloudutility.cache;

import org.junit.jupiter.api.Test;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CacheManagerTest {

    @Test
    void testCacheManager() {
        assertNotNull(new ConcurrentMapCacheManager());
    }
}
