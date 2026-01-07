package com.proveritus.cloudutility.mapper;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DtoEntityMapper<D, E> extends GenericMapper<E, D> {
}