package com.tinash.cloud.utility.jpa;

import com.tinash.cloud.utility.dto.response.PagedResponse;
import com.tinash.cloud.utility.mapper.BaseMapper;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Base REST controller with complete CRUD endpoints.
 * Extend this to get all REST operations automatically.
 * <p>
 * Provides:
 * - GET /           (findAll)
 * - GET /{id}       (findById)
 * - POST /          (create)
 * - PUT /{id}       (update)
 * - DELETE /{id}    (delete)
 *
 * @param <E>  Entity type
 * @param <D>  DTO type
 * @param <ID> ID type
 */
public abstract class BaseController<E, D, ID> {

    protected abstract BaseService<E, ID> getService();

    protected abstract BaseMapper<E, D> getMapper();

    /**
     * GET / - Find all with pagination
     */
    @GetMapping
    public ResponseEntity<PagedResponse<D>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String direction) {

        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        PagedResponse<E> entityPage = getService().findAll(pageable);
        PagedResponse<D> dtoPage = PagedResponse.<D>builder()
                .content(getMapper().toDtoList(entityPage.getContent()))
                .page(entityPage.getPage())
                .size(entityPage.getSize())
                .totalElements(entityPage.getTotalElements())
                .totalPages(entityPage.getTotalPages())
                .last(entityPage.isLast())
                .build();

        return ResponseEntity.ok(dtoPage);
    }

    /**
     * GET /{id} - Find by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<D>> findById(@PathVariable ID id) {
        E entity = getService().findById(id);
        D dto = getMapper().toDto(entity);
        return ResponseEntity.ok(ApiResponse.success(dto));
    }

    /**
     * POST / - Create new entity
     */
    @PostMapping
    public ResponseEntity<ApiResponse<D>> create(@Valid @RequestBody D dto) {
        E entity = getMapper().toEntity(dto);
        E saved = getService().create(entity);
        D savedDto = getMapper().toDto(saved);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(savedDto, "Created successfully"));
    }

    /**
     * PUT /{id} - Update entity
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<D>> update(@PathVariable ID id, @Valid @RequestBody D dto) {
        E entity = getMapper().toEntity(dto);
        E updated = getService().update(id, entity);
        D updatedDto = getMapper().toDto(updated);
        return ResponseEntity.ok(ApiResponse.success(updatedDto, "Updated successfully"));
    }

    /**
     * DELETE /{id} - Soft delete entity
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable ID id) {
        getService().delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Deleted successfully"));
    }

    /**
     * GET /all - Find all without pagination
     */
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<D>>> findAllNoPagination() {
        List<E> entities = getService().findAll();
        List<D> dtos = getMapper().toDtoList(entities);
        return ResponseEntity.ok(ApiResponse.success(dtos));
    }
}
