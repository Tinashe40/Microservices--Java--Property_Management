package com.proveritus.cloudutility.cache.key;

import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class CacheKeyResolver implements org.springframework.cache.interceptor.CacheResolver {

    @Override
    public Collection<org.springframework.cache.Cache> resolveCaches(org.springframework.cache.interceptor.CacheOperationInvocationContext<?> context) {
        // Implement custom logic to resolve caches
        return null;
    }
}