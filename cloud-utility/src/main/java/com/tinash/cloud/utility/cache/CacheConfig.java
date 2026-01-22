package com.tinash.cloud.utility.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Configures the application's cache manager using Caffeine.
     * <p>
     * This setup provides a default cache behavior:
     * - An initial capacity of 100 items.
     * - A maximum size of 500 items. After this, Caffeine will evict entries using a best-effort approach.
     * - Entries expire 10 minutes after the last write access.
     * <p>
     * This default configuration can be overridden in the `application.yml` of the consuming service
     * for more specific caching strategies per cache name.
     *
     * @return a configured CaffeineCacheManager.
     */
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(500)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .recordStats()); // Useful for monitoring cache performance
        return cacheManager;
    }
}
