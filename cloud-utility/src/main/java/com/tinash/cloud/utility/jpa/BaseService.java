package com.tinash.cloud.utility.jpa;

import com.tinash.cloud.utility.exception.business.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Base service with common CRUD operations.
 * Extend this class to inherit all basic operations.
 *
 * @param <T>  Entity type
 * @param <ID> ID type
 */
public abstract class BaseService<T, ID> {

    protected abstract BaseRepository<T, ID> getRepository();

    protected abstract String getEntityName();

    /**
     * Find entity by ID.
     */
    @Transactional(readOnly = true)
    public T findById(ID id) {
        return getRepository()
                .findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        getEntityName() + " not found with id: " + id));
    }

    /**
     * Find all entities.
     */
    @Transactional(readOnly = true)
    public List<T> findAll() {
        return getRepository().findByIsDeletedFalse();
    }

    /**
     * Find all with pagination.
     */
    @Transactional(readOnly = true)
    public PagedResponse<T> findAll(Pageable pageable) {
        Page<T> page = getRepository().findAll(pageable);
        return PagedResponse.<T>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .last(page.isLast())
                .build();
    }

    /**
     * Create new entity.
     */
    @Transactional
    public T create(T entity) {
        return getRepository().save(entity);
    }

    /**
     * Update existing entity.
     */
    @Transactional
    public T update(ID id, T entity) {
        T existing = findById(id);
        // Entity specific update logic should be in child class
        return getRepository().save(entity);
    }

    /**
     * Soft delete entity.
     */
    @Transactional
    public void delete(ID id) {
        T entity = findById(id);
        if (entity instanceof BaseEntity baseEntity) {
            baseEntity.setIsDeleted(true);
            getRepository().save(entity);
        }
    }

    /**
     * Hard delete entity (permanent).
     */
    @Transactional
    public void hardDelete(ID id) {
        getRepository().deleteById(id);
    }

    /**
     * Check if entity exists.
     */
    @Transactional(readOnly = true)
    public boolean exists(ID id) {
        return getRepository().existsById(id);
    }

    /**
     * Count all entities.
     */
    @Transactional(readOnly = true)
    public long count() {
        return getRepository().count();
    }
}