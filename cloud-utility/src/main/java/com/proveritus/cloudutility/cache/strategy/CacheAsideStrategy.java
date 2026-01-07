package com.proveritus.cloudutility.cache.strategy;

import org.springframework.cache.Cache;

import java.util.function.Supplier;

public class CacheAsideStrategy<K, V> implements CacheStrategy<K, V> {

    private final Cache cache;
    private final Supplier<V> valueLoader;

    public CacheAsideStrategy(Cache cache, Supplier<V> valueLoader) {
        this.cache = cache;
        this.valueLoader = valueLoader;
    }

    @Override
    public V get(K key) {
        Cache.ValueWrapper valueWrapper = cache.get(key);
        if (valueWrapper != null) {
            return (V) valueWrapper.get();
        }
        V value = valueLoader.get();
        cache.put(key, value);
        return value;
    }

    @Override
    public void put(K key, V value) {
        cache.put(key, value);
    }

    @Override
    public void evict(K key) {
        cache.evict(key);
    }
}