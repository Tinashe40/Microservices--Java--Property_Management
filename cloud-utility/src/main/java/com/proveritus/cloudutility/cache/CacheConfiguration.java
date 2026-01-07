package com.proveritus.cloudutility.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Multi-level caching configuration:
 * L1: Caffeine (local, fast)
 * L2: Redis (distributed, shared)
 */
@Configuration 
@EnableCaching 
@RequiredArgsConstructor
public class CacheConfiguration {

    /**
     * L1 Cache: Caffeine (local, in-memory).
     * Fast access, but not shared across instances.
     */
    @Bean 
    @ConditionalOnProperty(name = "cache.caffeine.enabled", havingValue = "true", matchIfMissing = true)
    public CacheManager caffeineCacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(10_000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .recordStats());
        
        return cacheManager;
    }

    /**
     * L2 Cache: Redis (distributed).
     * Shared across instances, slower than L1 but provides consistency.
     */
    @Bean 
    @Primary 
    @ConditionalOnProperty(name = "cache.redis.enabled", havingValue = "true")
    public CacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(
                                new GenericJackson2JsonRedisSerializer()))
                .disableCachingNullValues();

        // Different TTLs for different caches
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        cacheConfigurations.put("users", defaultConfig.entryTtl(Duration.ofMinutes(30)));
        cacheConfigurations.put("permissions", defaultConfig.entryTtl(Duration.ofHours(1)));
        cacheConfigurations.put("userGroups", defaultConfig.entryTtl(Duration.ofHours(1)));
        cacheConfigurations.put("passwordPolicies", defaultConfig.entryTtl(Duration.ofHours(24)));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }

    /**
     * Fallback cache manager (no external dependencies).
     */
    @Bean 
    @ConditionalOnProperty(
            name = {"cache.caffeine.enabled", "cache.redis.enabled"},
            havingValue = "false"
    )
    public CacheManager simpleCacheManager() {
        return new ConcurrentMapCacheManager("users", "permissions", "userGroups");
    }
}
