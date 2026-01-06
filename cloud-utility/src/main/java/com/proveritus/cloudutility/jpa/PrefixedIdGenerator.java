package com.proveritus.cloudutility.jpa;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.UUID;

public class PrefixedIdGenerator implements IdentifierGenerator {

    private static final String PREFIX = "PR";

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        return PREFIX + UUID.randomUUID().toString();
    }
}
