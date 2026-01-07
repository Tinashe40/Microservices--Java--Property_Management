package com.proveritus.cloudutility.mapper;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EntityDtoMapper<E, D> extends GenericMapper<E, D> {
}