package com.tinash.userservice.shared.domain.id;

public class PrefixedIdGenerator implements IdGenerator {

    private final IdGenerator delegate;
    private final String prefix;

    public PrefixedIdGenerator(IdGenerator delegate, String prefix) {
        this.delegate = delegate;
        this.prefix = prefix;
    }

    @Override
    public String generate() {
        return prefix + "_" + delegate.generate();
    }
}
