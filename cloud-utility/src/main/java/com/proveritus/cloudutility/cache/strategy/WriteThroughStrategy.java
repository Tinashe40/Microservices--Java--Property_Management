package com.proveritus.cloudutility.cache.strategy;

import org.springframework.cache.Cache;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class WriteThroughStrategy<K, V> implements CacheStrategy<K, V> {

    private final Cache cache;
    private final BiConsumer<K, V> writer;
    private final Consumer<K> remover;

    public WriteThroughStrategy(Cache cache, BiConsumer<K, V> writer, Consumer<K> remover) {
        this.cache = cache;
        this.writer = writer;
        this.remover = remover;
    }

    @Override
    public V get(K key) {
        Cache.ValueWrapper valueWrapper = cache.get(key);
        if (valueWrapper != null) {
            return (V) valueWrapper.get();
        }
        return null;
    }

    @Override
    public void put(K key, V value) {
        writer.accept(key, value);
        cache.put(key, value);
    }

    @Override
    public void evict(K key) {
        remover.accept(key);
        cache.evict(key);
    }
}