package com.tinash.propertyservice.property.mapper;

import com.tinash.cloud.utility.jpa.EntityDtoMapper;
import com.tinash.propertyservice.floor.mapper.FloorMapper;
import com.tinash.propertyservice.property.domain.Property;
import com.tinash.propertyservice.property.dto.PropertyDTO;
import com.tinash.propertyservice.unit.mapper.UnitMapper;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {FloorMapper.class, UnitMapper.class})
public interface PropertyMapper extends EntityDtoMapper<PropertyDTO, Property, PropertyDTO, PropertyDTO> {

    @org.mapstruct.Mapping(target = "managedByDetails", ignore = true)
    @org.mapstruct.Mapping(target = "floors", ignore = true)
    @org.mapstruct.Mapping(target = "units", ignore = true)
    PropertyDTO toDto(Property property);

    List<PropertyDTO> toDto(List<Property> properties);

    Property fromCreateDto(PropertyDTO dto);

    @org.mapstruct.Mapping(target = "createdBy", ignore = true)
    @org.mapstruct.Mapping(target = "createdDate", ignore = true)
    @org.mapstruct.Mapping(target = "lastModifiedBy", ignore = true)
    @org.mapstruct.Mapping(target = "lastModifiedDate", ignore = true)
    @org.mapstruct.Mapping(target = "version", ignore = true)
    @org.mapstruct.Mapping(target = "deleted", ignore = true)
    void updateFromUpdateDto(PropertyDTO dto, @MappingTarget Property entity);
}
