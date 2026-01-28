package com.proveritus.userservice.shared.domain.id;

import org.springframework.stereotype.Component;

/**
 * Factory for creating ID generators
 * Single Responsibility: Create appropriate ID generators
 */
@Component
public class IdGeneratorFactory {

    private final IdGenerator baseGenerator;

    public IdGeneratorFactory(IdGenerator baseGenerator) {
        this.baseGenerator = baseGenerator;
    }

    public IdGenerator createPrefixed(String prefix) {
        return new PrefixedIdGenerator(baseGenerator, prefix);
    }
}
