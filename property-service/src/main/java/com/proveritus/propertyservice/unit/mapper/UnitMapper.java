package com.proveritus.propertyservice.unit.mapper;

import com.proveritus.cloudutility.jpa.EntityDtoMapper;
import com.proveritus.propertyservice.unit.domain.Unit;
import com.proveritus.propertyservice.unit.dto.UnitDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UnitMapper extends EntityDtoMapper<UnitDTO, Unit, UnitDTO, UnitDTO> {

    @Mapping(source = "property.id", target = "propertyId")
    @Mapping(source = "floor.id", target = "floorId")
    UnitDTO toDto(Unit unit);

    List<UnitDTO> toDto(List<Unit> units);

    @Mapping(target = "property", ignore = true)
    @Mapping(target = "floor", ignore = true)
    Unit fromCreateDto(UnitDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "property", ignore = true)
    @Mapping(target = "floor", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateFromUpdateDto(UnitDTO dto, @MappingTarget Unit entity);
}