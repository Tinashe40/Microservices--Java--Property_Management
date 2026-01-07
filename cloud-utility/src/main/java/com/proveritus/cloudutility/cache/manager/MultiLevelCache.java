package com.proveritus.cloudutility.cache.manager;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.List;
import java.util.concurrent.Callable;

public class MultiLevelCache implements Cache {

    private final String name;
    private final List<CacheManager> cacheManagers;

    public MultiLevelCache(String name, List<CacheManager> cacheManagers) {
        this.name = name;
        this.cacheManagers = cacheManagers;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getNativeCache() {
        return cacheManagers;
    }

    @Override
    public ValueWrapper get(Object key) {
        for (CacheManager cacheManager : cacheManagers) {
            Cache cache = cacheManager.getCache(name);
            if (cache != null) {
                ValueWrapper valueWrapper = cache.get(key);
                if (valueWrapper != null) {
                    return valueWrapper;
                }
            }
        }
        return null;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        for (CacheManager cacheManager : cacheManagers) {
            Cache cache = cacheManager.getCache(name);
            if (cache != null) {
                T value = cache.get(key, type);
                if (value != null) {
                    return value;
                }
            }
        }
        return null;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        for (CacheManager cacheManager : cacheManagers) {
            Cache cache = cacheManager.getCache(name);
            if (cache != null) {
                try {
                    return cache.get(key, valueLoader);
                } catch (Exception e) {
                    // ignore and try next level
                }
            }
        }
        return null;
    }

    @Override
    public void put(Object key, Object value) {
        for (CacheManager cacheManager : cacheManagers) {
            Cache cache = cacheManager.getCache(name);
            if (cache != null) {
                cache.put(key, value);
            }
        }
    }

    @Override
    public void evict(Object key) {
        for (CacheManager cacheManager : cacheManagers) {
            Cache cache = cacheManager.getCache(name);
            if (cache != null) {
                cache.evict(key);
            }
        }
    }

    @Override
    public void clear() {
        for (CacheManager cacheManager : cacheManagers) {
            Cache cache = cacheManager.getCache(name);
            if (cache != null) {
                cache.clear();
            }
        }
    }
}