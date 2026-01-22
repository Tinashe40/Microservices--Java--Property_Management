package com.tinash.cloud.utility.dto.response;

import lombok.Data;
import lombok.*;

import java.util.List;

/**
 * Standard paginated response wrapper.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagedResponse<T> {

    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
