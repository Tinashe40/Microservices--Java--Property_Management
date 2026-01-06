package com.proveritus.cloudutility.core.port.out;

import com.proveritus.cloudutility.core.domain.BaseEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Command operations for write access.
 */
interface CommandRepository<T extends BaseEntity<ID>, ID extends Serializable> {

    T save(T entity);

    List<T> saveAll(Iterable<T> entities);

    void deleteById(ID id);

    void delete(T entity);

    void deleteAll(Iterable<T> entities);
}