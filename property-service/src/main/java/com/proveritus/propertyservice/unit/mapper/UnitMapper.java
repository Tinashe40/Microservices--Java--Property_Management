package com.proveritus.propertyservice.unit.mapper;

import com.proveritus.cloudutility.jpa.EntityDtoMapper;
import com.proveritus.propertyservice.unit.domain.Unit;
import com.proveritus.propertyservice.unit.dto.request.CreateUnitRequest;
import com.proveritus.propertyservice.unit.dto.request.UpdateUnitRequest;
import com.proveritus.propertyservice.unit.dto.response.UnitResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UnitMapper extends EntityDtoMapper<UnitResponse, Unit, CreateUnitRequest, UpdateUnitRequest> {
}
