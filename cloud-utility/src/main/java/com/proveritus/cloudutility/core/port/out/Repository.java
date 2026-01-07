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
