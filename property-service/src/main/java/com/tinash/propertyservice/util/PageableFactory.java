package com.tinash.propertyservice.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public final class PageableFactory {

    private PageableFactory() {
    }

    public static Pageable createPageable(int page, int size, String sortBy, String direction) {
        if (size <= 0) {
            return Pageable.unpaged();
        }
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc")
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        return PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
    }

    public static Pageable createPageable(int page, int size) {
        if (size <= 0) {
            return Pageable.unpaged();
        }
        return PageRequest.of(page, size);
    }
}
