package com.proveritus.cloudutility.cache.strategy;

import org.springframework.cache.Cache;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class WriteBehindStrategy<K, V> implements CacheStrategy<K, V> {

    private final Cache cache;
    private final BiConsumer<K, V> writer;
    private final Consumer<K> remover;
    private final ExecutorService executorService;

    public WriteBehindStrategy(Cache cache, BiConsumer<K, V> writer, Consumer<K> remover) {
        this.cache = cache;
        this.writer = writer;
        this.remover = remover;
        this.executorService = Executors.newFixedThreadPool(4);
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
        cache.put(key, value);
        executorService.submit(() -> writer.accept(key, value));
    }

    @Override
    public void evict(K key) {
        cache.evict(key);
        executorService.submit(() -> remover.accept(key));
    }
}