package com.proveritus.cloudutility.jpa;

import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * A generic mapper interface for converting between DTOs/commands and Entities.
 *
 * @param <D> The response DTO type.
 * @param <E> The domain type.
 * @param <C> The create DTO/command type.
 * @param <U> The update DTO/command type.
 */
public interface EntityDtoMapper<D, E, C, U> {

    /**
     * Maps an domain to a response DTO.
     *
     * @param entity The domain.
     * @return The response DTO.
     */
    D toDto(E entity);

    /**
     * Maps a create DTO/command to a new domain.
     *
     * @param createDto The create DTO/command.
     * @return A new domain instance.
     */
    E fromCreateDto(C createDto);

    /**
     * Updates an existing domain from an update DTO/command.
     *
     * @param updateDto The update DTO/command.
     * @param entity    The domain to be updated.
     */
    void updateFromUpdateDto(U updateDto, @MappingTarget E entity);
}