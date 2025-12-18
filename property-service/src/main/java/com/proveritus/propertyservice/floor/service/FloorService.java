package com.proveritus.propertyservice.floor.service;

import com.proveritus.cloudutility.exception.ResourceNotFoundException;
import com.proveritus.cloudutility.jpa.DomainService;
import com.proveritus.propertyservice.floor.domain.Floor;
import com.proveritus.propertyservice.floor.dto.FloorDTO;
import com.proveritus.propertyservice.floor.dto.FloorOccupancyStats;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FloorService extends DomainService<Floor, FloorDTO, FloorDTO, FloorDTO> {
//    List<FloorDTO> getFloorsByPropertyId(Long propertyId);
    Page<FloorDTO> getFloorsByPropertyId(Long propertyId, Pageable pageable);
    void createFloors(List<FloorDTO> floorDTOs);
    void updateFloors(List<FloorDTO> floorDTOs);
    void deleteFloors(List<Long> ids);
    FloorOccupancyStats getFloorOccupancyStats(Long id) throws ResourceNotFoundException;
    void updateFloorOccupancyStats(Long floorId);
}

