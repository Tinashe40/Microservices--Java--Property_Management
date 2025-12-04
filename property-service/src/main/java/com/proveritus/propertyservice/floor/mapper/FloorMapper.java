package com.proveritus.propertyservice.floor.mapper;

import com.proveritus.cloudutility.jpa.EntityDtoMapper;
import com.proveritus.propertyservice.floor.domain.Floor;
import com.proveritus.propertyservice.floor.dto.request.CreateFloorRequest;
import com.proveritus.propertyservice.floor.dto.request.UpdateFloorRequest;
import com.proveritus.propertyservice.floor.dto.response.FloorResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FloorMapper extends EntityDtoMapper<FloorResponse, Floor, CreateFloorRequest, UpdateFloorRequest> {
}
