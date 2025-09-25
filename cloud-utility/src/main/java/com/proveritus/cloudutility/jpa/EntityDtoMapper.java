package com.proveritus.cloudutility.jpa;

import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * A generic mapper interface for converting between DTOs/commands and Entities.
 *
 * @param <D> The response DTO type.
 * @param <E> The entity type.
 * @param <C> The create DTO/command type.
 * @param <U> The update DTO/command type.
 */
public interface EntityDtoMapper<D, E, C, U> {

    /**
     * Maps an entity to a response DTO.
     *
     * @param entity The entity.
     * @return The response DTO.
     */
    D toDto(E entity);

    /**
     * Maps a list of entities to a list of response DTOs.
     *
     * @param entities The list of entities.
     * @return The list of response DTOs.
     */
    List<D> toDto(List<E> entities);

    /**
     * Maps a create DTO/command to a new entity.
     *
     * @param createDto The create DTO/command.
     * @return A new entity instance.
     */
    E fromCreateDto(C createDto);

    /**
     * Updates an existing entity from an update DTO/command.
     *
     * @param updateDto The update DTO/command.
     * @param entity    The entity to be updated.
     */
    void updateFromUpdateDto(U updateDto, @MappingTarget E entity);
}