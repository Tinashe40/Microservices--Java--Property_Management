package com.proveritus.cloudutility.cache.serializer;

public interface CacheSerializer<T> {

    byte[] serialize(T object);

    T deserialize(byte[] bytes);
}