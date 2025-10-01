package com.proveritus.cloudutility.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;

/**
 * A generic service interface for CRUD operations.
 *
 * @param <T> The entity type.
 * @param <C> The create DTO/command type.
 * @param <U> The update DTO/command type.
 * @param <D> The response DTO type.
 */
public interface DomainService<T, C, U extends Updatable, D> {

    D create(C createCommand);

    /**
     * Updates an existing entity.
     *
     * @param updateCommand The DTO/command for update.
     * @return The DTO of the updated entity.
     */
    D update(U updateCommand);

    /**
     * Finds an entity by its ID.
     *
     * @param id The ID of the entity.
     * @return The DTO of the found entity.
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
     * Deletes an entity by its ID.
     *
     * @param id The ID of the entity to delete.
     */
    void deleteById(Long id);

}