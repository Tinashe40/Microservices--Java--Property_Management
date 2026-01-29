package com.tinash.propertyservice.floor.service;

import com.tinash.cloud.utility.exception.ResourceNotFoundException;
import com.tinash.cloud.utility.jpa.DomainService;
import com.tinash.propertyservice.floor.domain.Floor;
import com.tinash.propertyservice.floor.dto.FloorDTO;
import com.tinash.propertyservice.floor.dto.FloorOccupancyStats;

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

