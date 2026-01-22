package com.tinash.cloud.utility.jpa.id;

import com.github.f4b6a3.ulid.UlidCreator;
import org.springframework.stereotype.Component;

/**
 * ULID-based implementation
 * Single Responsibility: Generate ULID identifiers
 */
@Component
public class UlidGenerator implements IdGenerator {

    @Override
    public String generate() {
        return UlidCreator.getUlid().toString();
    }
}
