package com.tinash.cloud.utility.mapper;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DtoEntityMapper<D, E> extends BaseMapper<E, D> {
}