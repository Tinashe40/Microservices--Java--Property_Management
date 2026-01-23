package com.tinash.cloud.utility.mapper;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EntityDtoMapper<E, D> extends BaseMapper<E, D> {
}