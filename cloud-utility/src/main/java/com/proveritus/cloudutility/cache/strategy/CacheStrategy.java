package com.proveritus.cloudutility.cache.strategy;

public interface CacheStrategy<K, V> {

    V get(K key);

    void put(K key, V value);

    void evict(K key);
}