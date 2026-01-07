package com.proveritus.cloudutility.cache.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;

@Component
public class KryoSerializer<T> implements CacheSerializer<T> {

    private final Kryo kryo = new Kryo();
    private final Class<T> clazz;

    public KryoSerializer(Class<T> clazz) {
        this.clazz = clazz;
        this.kryo.register(clazz);
    }

    @Override
    public byte[] serialize(T object) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Output output = new Output(baos);
        kryo.writeObject(output, object);
        output.close();
        return baos.toByteArray();
    }

    @Override
    public T deserialize(byte[] bytes) {
        Input input = new Input(bytes);
        T object = kryo.readObject(input, clazz);
        input.close();
        return object;
    }
}