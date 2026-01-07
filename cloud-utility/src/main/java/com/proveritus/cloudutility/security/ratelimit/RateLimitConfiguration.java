package com.proveritus.cloudutility.security.ratelimit;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.function.Supplier;

/**
 * Rate limiting configuration using Bucket4j with Redis for distributed rate limiting.
 */
@Configuration
public class RateLimitConfiguration {

    /**
     * Creates a proxy manager for distributed rate limiting with Redis.
     */
    @Bean
    public ProxyManager<String> proxyManager(RedisClient redisClient) {
        StatefulRedisConnection<String, byte[]> connection = redisClient.connect();
        return LettuceBasedProxyManager.builderFor(connection)
                .build();
    }

    /**
     * Bucket configuration supplier for different rate limit tiers.
     */
    public static Supplier<BucketConfiguration> globalLimitBucketConfiguration() {
        return () -> BucketConfiguration.builder()
                .addLimit(Bandwidth.simple(1000, Duration.ofMinutes(1)))
                .build();
    }

    public static Supplier<BucketConfiguration> userLimitBucketConfiguration() {
        return () -> BucketConfiguration.builder()
                .addLimit(Bandwidth.simple(100, Duration.ofMinutes(1)))
                .build();
    }

    public static Supplier<BucketConfiguration> authLimitBucketConfiguration() {
        return () -> BucketConfiguration.builder()
                .addLimit(Bandwidth.simple(5, Duration.ofMinutes(1)))
                .addLimit(Bandwidth.simple(20, Duration.ofHours(1)))
                .build();
    }
}
