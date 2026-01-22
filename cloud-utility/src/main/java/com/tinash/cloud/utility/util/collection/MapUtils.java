package com.tinash.cloud.utility.util.collection;

import java.util.Map;

public final class MapUtils {

    private MapUtils() {
    }

    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }
}
