package com.proveritus.cloudutility.core.port.out;

import com.proveritus.cloudutility.core.domain.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Query operations for read-only access.
 */
interface QueryRepository<T extends BaseEntity<ID>, ID extends Serializable> {

    Optional<T> findById(ID id);

    List<T> findAll();

    Page<T> findAll(Pageable pageable);

    boolean existsById(ID id);

    long count();
}
