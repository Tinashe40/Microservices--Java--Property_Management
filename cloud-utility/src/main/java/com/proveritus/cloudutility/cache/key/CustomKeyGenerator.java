package com.proveritus.cloudutility.cache.key;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

@Component
public class CustomKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        return new StringBuilder()
                .append(target.getClass().getSimpleName())
                .append(".")
                .append(method.getName())
                .append(Arrays.toString(params))
                .toString();
    }
}