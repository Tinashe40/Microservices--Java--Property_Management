package com.proveritus.cloudutility.core.port.out;

import com.proveritus.cloudutility.core.domain.BaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Generic repository port following ISP (Interface Segregation Principle).
 * Split into query and command operations for better control.
 *
 * @param <T>  the entity type
 * @param <ID> the ID type
 */
public interface Repository<T extends BaseEntity<ID>, ID extends Serializable> 
        extends QueryRepository<T, ID>, CommandRepository<T, ID> {
}
