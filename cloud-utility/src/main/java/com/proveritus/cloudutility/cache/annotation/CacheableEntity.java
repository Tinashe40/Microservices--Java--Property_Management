package com.proveritus.cloudutility.cache.annotation;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * Custom cacheable annotation with sensible defaults.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Cacheable
public @interface CacheableEntity {

    @AliasFor(annotation = Cacheable.class, attribute = "cacheNames")
    String[] value() default {};

    @AliasFor(annotation = Cacheable.class, attribute = "key")
    String key() default "";

    /**
     * Cache TTL in seconds.
     */
    int ttl() default 600; // 10 minutes default

    /**
     * Whether to cache null values.
     */
    boolean cacheNull() default false;
}