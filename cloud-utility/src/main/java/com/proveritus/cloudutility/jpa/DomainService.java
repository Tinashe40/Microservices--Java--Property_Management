package com.proveritus.cloudutility.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

/**
 * A generic service interface for CRUD operations.

 * @param <T> The domain type.
 * @param <C> The create DTO/command type.
 * @param <U> The update DTO/command type.
 * @param <D> The response DTO type.
 */
public interface DomainService<T, C, U extends Updatable, D> {

    D create(C createCommand);

    /**
     * Updates an existing domain.
     *
     * @param updateCommand The DTO/command for update.
     * @return The DTO of the updated domain.
     */
    D update(U updateCommand);

    /**
     * Finds an domain by its ID.
     *
     * @param id The ID of the domain.
     * @return The DTO of the found domain.
     */
    D findById(Long id);

    /**
     * Finds all entities.
     *
     * @return A collection of DTOs.
     */
    Collection<D> findAll();

    /**
     * Finds all entities in a paginated way.
     *
     * @param pageable The pagination information.
     * @return A page of DTOs.
     */
    Page<D> findAll(Pageable pageable);

    /**
     * Deletes an domain by its ID.
     *
     * @param id The ID of the domain to delete.
     */
    void deleteById(Long id);

}