package com.proveritus.propertyservice.floor.service;

import com.proveritus.propertyservice.floor.dto.FloorDTO;
import com.proveritus.propertyservice.floor.dto.FloorOccupancyStats;
import com.proveritus.cloudutility.exception.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FloorService {
    FloorDTO createFloor(FloorDTO floorDTO);
    FloorDTO getFloorById(Long id) throws EntityNotFoundException;
    List<FloorDTO> getFloorsByPropertyId(Long propertyId);
    FloorDTO updateFloor(Long id, FloorDTO floorDTO) throws EntityNotFoundException;
    Page<FloorDTO> getFloorsByPropertyId(Long propertyId, Pageable pageable);
    void deleteFloor(Long id) throws EntityNotFoundException;
    void createFloors(List<FloorDTO> floorDTOs);
    void updateFloors(List<FloorDTO> floorDTOs);
    void deleteFloors(List<Long> ids);
    FloorOccupancyStats getFloorOccupancyStats(Long id) throws EntityNotFoundException;
    void updateFloorOccupancyStats(Long floorId);
}

