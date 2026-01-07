package com.proveritus.cloudutility.core.port.out;

import com.proveritus.cloudutility.core.domain.AggregateRoot;

import java.io.Serializable;
import java.util.Optional;

public interface PersistencePort<T extends AggregateRoot<ID>, ID extends Serializable> {

    T save(T aggregate);

    Optional<T> findById(ID id);

    void delete(T aggregate);
}