package com.tinash.cloud.utility.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * Base Mapper interface for MapStruct.
 * Provides common mapping operations between an Entity (E) and a DTO (D).
 * Specific mappers can extend this interface to inherit basic mappings and
 * add domain-specific transformations.
 *
 * @param <E> The Entity type.
 * @param <D> The DTO type.
 */
public interface BaseMapper<E, D> {

    /**
     * Maps an entity to its corresponding DTO.
     *
     * @param entity The entity to map.
     * @return The mapped DTO.
     */
    D toDto(E entity);

    /**
     * Maps a DTO to its corresponding entity.
     *
     * @param dto The DTO to map.
     * @return The mapped entity.
     */
    E toEntity(D dto);

    /**
     * Maps a list of entities to a list of DTOs.
     *
     * @param entityList The list of entities to map.
     * @return The list of mapped DTOs.
     */
    List<D> toDtoList(List<E> entityList);

    /**
     * Maps a list of DTOs to a list of entities.
     *
     * @param dtoList The list of DTOs to map.
     * @return The list of mapped entities.
     */
    List<E> toEntityList(List<D> dtoList);

    /**
     * Updates an existing entity with values from a DTO.
     * This is useful for partial updates where the existing entity should be modified.
     *
     * @param dto The DTO containing updated values.
     * @param entity The entity to update.
     */
    @InheritConfiguration
    void updateEntityFromDto(D dto, @MappingTarget E entity);

    /**
     * Updates an existing DTO with values from an entity.
     *
     * @param entity The entity containing updated values.
     * @param dto The DTO to update.
     */
    @InheritInverseConfiguration
    void updateDtoFromEntity(E entity, @MappingTarget D dto);
}
