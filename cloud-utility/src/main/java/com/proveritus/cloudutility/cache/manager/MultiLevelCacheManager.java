package com.proveritus.cloudutility.cache.manager;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class MultiLevelCacheManager implements CacheManager {

    private final List<CacheManager> cacheManagers;

    public MultiLevelCacheManager(List<CacheManager> cacheManagers) {
        this.cacheManagers = cacheManagers;
    }

    @Override
    public Cache getCache(String name) {
        return new MultiLevelCache(name, cacheManagers);
    }

    @Override
    public Collection<String> getCacheNames() {
        return cacheManagers.stream()
                .map(CacheManager::getCacheNames)
                .flatMap(Collection::stream)
                .distinct()
                .toList();
    }
}