package com.proveritus.propertyservice.unit.service;

import com.proveritus.cloudutility.enums.OccupancyStatus;
import com.proveritus.cloudutility.exception.ResourceNotFoundException;
import com.proveritus.cloudutility.jpa.DomainService;
import com.proveritus.propertyservice.unit.domain.Unit;
import com.proveritus.propertyservice.unit.dto.UnitDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UnitService extends DomainService<Unit, UnitDTO, UnitDTO, UnitDTO> {
    UnitDTO getUnitByNameAndPropertyId(String name, Long propertyId) throws ResourceNotFoundException;
    Page<UnitDTO> getUnitsByPropertyId(Long propertyId, Pageable pageable);
    Page<UnitDTO> getUnitsByFloorId(Long floorId, Pageable pageable);
    Page<UnitDTO> getUnitsWithFilters(Long propertyId, Long floorId, OccupancyStatus occupancyStatus, Pageable pageable);
    void createUnits(List<UnitDTO> unitDTOs);
    void updateUnits(List<UnitDTO> unitDTOs);
    void deleteUnits(List<Long> ids);
    UnitDTO updateOccupancyStatus(Long id, OccupancyStatus occupancyStatus, String tenant) throws ResourceNotFoundException;
    Page<UnitDTO> searchUnits(String query, Pageable pageable);
    Double calculatePotentialRentalIncome(Long propertyId);
    long countUnitsByPropertyId(Long propertyId);
}

