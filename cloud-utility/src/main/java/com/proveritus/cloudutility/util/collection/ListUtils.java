package com.proveritus.cloudutility.util.collection;

import java.util.List;

public final class ListUtils {

    private ListUtils() {
    }

    public static <T> List<T> emptyIfNull(List<T> list) {
        return list == null ? java.util.Collections.emptyList() : list;
    }
}
