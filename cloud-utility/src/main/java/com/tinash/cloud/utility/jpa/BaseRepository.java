package com.tinash.cloud.utility.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

/**
 * Base repository interface with common query methods.
 * Provides JPA and Specification support.
 *
 * @param <T>  Entity type
 * @param <ID> ID type
 */
@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

    /**
     * Find all non-deleted entities.
     */
    List<T> findByIsDeletedFalse();
    Page<T> findAll(Pageable pageable);

    /**
     * Find by ID (non-deleted only).
     */
    Optional<T> findByIdAndIsDeletedFalse(ID id);
}