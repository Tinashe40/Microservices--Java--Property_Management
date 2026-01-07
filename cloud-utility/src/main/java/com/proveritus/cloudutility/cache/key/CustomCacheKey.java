package com.proveritus.cloudutility.cache.key;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

public class CustomCacheKey implements Serializable {

    private final Class<?> targetClass;
    private final String methodName;
    private final Object[] params;

    public CustomCacheKey(Object target, Method method, Object[] params) {
        this.targetClass = target.getClass();
        this.methodName = method.getName();
        this.params = params;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        CustomCacheKey other = (CustomCacheKey) obj;
        return targetClass.equals(other.targetClass) &&
                methodName.equals(other.methodName) &&
                Arrays.equals(params, other.params);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + targetClass.hashCode();
        result = 31 * result + methodName.hashCode();
        result = 31 * result + Arrays.hashCode(params);
        return result;
    }
}
