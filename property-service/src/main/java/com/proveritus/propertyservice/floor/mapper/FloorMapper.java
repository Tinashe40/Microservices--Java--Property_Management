package com.proveritus.propertyservice.floor.mapper;

import com.tinash.cloud.utility.jpa.EntityDtoMapper;
import com.proveritus.propertyservice.floor.domain.Floor;
import com.proveritus.propertyservice.floor.dto.FloorDTO;
import com.proveritus.propertyservice.unit.mapper.UnitMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = UnitMapper.class)
public interface FloorMapper extends EntityDtoMapper<FloorDTO, Floor, FloorDTO, FloorDTO> {

    @Mapping(source = "property.id", target = "propertyId")
    @Mapping(target = "propertyType", ignore = true)
    @Mapping(target = "address", ignore = true)
    FloorDTO toDto(Floor floor);

    List<FloorDTO> toDto(List<Floor> floors);

    @Mapping(target = "property", ignore = true)
    Floor fromCreateDto(FloorDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "property", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    void updateFromUpdateDto(FloorDTO dto, @MappingTarget Floor entity);
}