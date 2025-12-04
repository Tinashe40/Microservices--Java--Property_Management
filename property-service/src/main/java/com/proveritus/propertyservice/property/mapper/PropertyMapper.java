package com.proveritus.propertyservice.property.mapper;

import com.proveritus.cloudutility.jpa.EntityDtoMapper;
import com.proveritus.propertyservice.property.domain.Property;
import com.proveritus.propertyservice.property.dto.request.CreatePropertyRequest;
import com.proveritus.propertyservice.property.dto.request.UpdatePropertyRequest;
import com.proveritus.propertyservice.property.dto.response.PropertyResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PropertyMapper extends EntityDtoMapper<PropertyResponse, Property, CreatePropertyRequest, UpdatePropertyRequest> {
}
