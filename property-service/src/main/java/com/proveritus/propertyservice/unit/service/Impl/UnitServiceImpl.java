package com.proveritus.propertyservice.unit.service.Impl;

import com.proveritus.cloudutility.enums.*;
import com.proveritus.propertyservice.floor.domain.Floor;
import com.proveritus.propertyservice.property.domain.Property;
import com.proveritus.propertyservice.unit.domain.Unit;
import com.proveritus.cloudutility.enums.RentType;
import com.proveritus.cloudutility.exception.EntityNotFoundException;
import com.proveritus.propertyservice.floor.domain.FloorRepository;
import com.proveritus.propertyservice.property.domain.PropertyRepository;
import com.proveritus.propertyservice.unit.domain.UnitRepository;
import com.proveritus.propertyservice.floor.service.FloorService;
import com.proveritus.propertyservice.unit.domain.UnitValidator;
import com.proveritus.propertyservice.unit.dto.UnitDTO;
import com.proveritus.propertyservice.unit.service.UnitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UnitServiceImpl implements UnitService {
    private final UnitRepository unitRepository;
    private final PropertyRepository propertyRepository;
    private final FloorRepository floorRepository;
    private final ModelMapper modelMapper;
    private final FloorService floorService;
    private final UnitValidator unitValidator;

    @Override
    @CacheEvict(value = "units", allEntries = true)
    public UnitDTO createUnit(UnitDTO unitDTO) {
        log.info("Creating new unit: {}", unitDTO.getName());
        unitValidator.validate(unitDTO);

        Property property = getPropertyById(unitDTO.getPropertyId());
        Floor floor = getFloorIfProvided(unitDTO.getFloorId());

        calculateMonthlyRent(unitDTO);

        Unit unit = modelMapper.map(unitDTO, Unit.class);
        unit.setProperty(property);
        unit.setFloor(floor);

        Unit savedUnit = unitRepository.save(unit);
        updateFloorOccupancyIfNeeded(unitDTO.getFloorId());

        log.debug("Unit created successfully with ID: {}", savedUnit.getId());
        return modelMapper.map(savedUnit, UnitDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "units", key = "#id")
    public UnitDTO getUnitById(Long id) {
        log.debug("Fetching unit with ID: {}", id);
        return unitRepository.findById(id)
                .map(unit -> modelMapper.map(unit, UnitDTO.class))
                .orElseThrow(() -> new EntityNotFoundException("Unit not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public UnitDTO getUnitByNameAndPropertyId(String name, Long propertyId) {
        log.debug("Fetching unit with name: {} in property ID: {}", name, propertyId);
        return unitRepository.findByNameAndPropertyId(name, propertyId)
                .map(unit -> modelMapper.map(unit, UnitDTO.class))
                .orElseThrow(() -> new EntityNotFoundException("Unit not found with name: " + name + " in property ID: " + propertyId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UnitDTO> getUnitsByPropertyId(Long propertyId) {
        log.debug("Fetching all units for property ID: {}", propertyId);
        validatePropertyExists(propertyId);
        return unitRepository.findByPropertyId(propertyId).stream()
                .map(unit -> modelMapper.map(unit, UnitDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UnitDTO> getUnitsByPropertyId(Long propertyId, Pageable pageable) {
        log.debug("Fetching paginated units for property ID: {}", propertyId);
        validatePropertyExists(propertyId);
        return unitRepository.findByPropertyId(propertyId, pageable)
                .map(unit -> modelMapper.map(unit, UnitDTO.class));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UnitDTO> getUnitsByFloorId(Long floorId) {
        log.debug("Fetching all units for floor ID: {}", floorId);
        validateFloorExists(floorId);
        return unitRepository.findByFloorId(floorId).stream()
                .map(unit -> modelMapper.map(unit, UnitDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UnitDTO> getUnitsByFloorId(Long floorId, Pageable pageable) {
        log.debug("Fetching paginated units for floor ID: {}", floorId);
        validateFloorExists(floorId);
        return unitRepository.findByFloorId(floorId, pageable)
                .map(unit -> modelMapper.map(unit, UnitDTO.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UnitDTO> getUnitsWithFilters(Long propertyId, Long floorId, OccupancyStatus occupancyStatus, Pageable pageable) {
        log.debug("Fetching units with filters - Property ID: {}, Floor ID: {}, Occupancy Status: {}",
                propertyId, floorId, occupancyStatus);

        if (propertyId != null) validatePropertyExists(propertyId);
        if (floorId != null) validateFloorExists(floorId);

        return unitRepository.findWithFilters(propertyId, floorId, occupancyStatus, pageable)
                .map(unit -> modelMapper.map(unit, UnitDTO.class));
    }

    @Override
    @CacheEvict(value = "units", allEntries = true)
    public UnitDTO updateUnit(Long id, UnitDTO unitDTO) {
        log.info("Updating unit with ID: {}", id);
        Unit existingUnit = findUnitById(id);
        unitValidator.validate(unitDTO, id);

        Property property = getPropertyById(unitDTO.getPropertyId());
        Floor floor = getFloorIfProvided(unitDTO.getFloorId());

        calculateMonthlyRent(unitDTO);

        modelMapper.map(unitDTO, existingUnit);
        existingUnit.setProperty(property);
        existingUnit.setFloor(floor);

        Unit updatedUnit = unitRepository.save(existingUnit);
        updateFloorOccupancyIfNeeded(unitDTO.getFloorId());

        log.debug("Unit updated successfully with ID: {}", id);
        return modelMapper.map(updatedUnit, UnitDTO.class);
    }

    @Override
    @CacheEvict(value = "units", allEntries = true)
    public void deleteUnit(Long id) {
        log.info("Deleting unit with ID: {}", id);
        Unit unit = findUnitById(id);
        Long floorId = (unit.getFloor() != null) ? unit.getFloor().getId() : null;

        unitRepository.deleteById(id);
        updateFloorOccupancyIfNeeded(floorId);

        log.debug("Unit deleted successfully with ID: {}", id);
    }

    @Override
    @CacheEvict(value = "units", allEntries = true)
    public UnitDTO updateOccupancyStatus(Long id, OccupancyStatus occupancyStatus, String tenant) {
        log.info("Updating occupancy status for unit ID: {} to {}", id, occupancyStatus);
        Unit unit = findUnitById(id);

        unit.setOccupancyStatus(occupancyStatus);
        unit.setTenant(OccupancyStatus.OCCUPIED.equals(occupancyStatus) ? tenant : null);

        Unit updatedUnit = unitRepository.save(unit);
        updateFloorOccupancyIfNeeded(unit.getFloor() != null ? unit.getFloor().getId() : null);

        log.debug("Occupancy status updated successfully for unit ID: {}", id);
        return modelMapper.map(updatedUnit, UnitDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UnitDTO> searchUnits(String query) {
        log.debug("Searching units by query: {}", query);
        return unitRepository.searchUnits(query, Pageable.unpaged()).getContent().stream()
                .map(unit -> modelMapper.map(unit, UnitDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UnitDTO> searchUnits(String query, Pageable pageable) {
        log.debug("Searching units by query with pagination: {}", query);
        return unitRepository.searchUnits(query, pageable)
                .map(unit -> modelMapper.map(unit, UnitDTO.class));
    }

    @Override
    @Transactional(readOnly = true)
    public Double calculatePotentialRentalIncome(Long propertyId) {
        log.debug("Calculating potential rental income for property ID: {}", propertyId);
        validatePropertyExists(propertyId);
        return unitRepository.calculateTotalRentalIncome(propertyId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countUnitsByPropertyId(Long propertyId) {
        log.debug("Counting units for property ID: {}", propertyId);
        validatePropertyExists(propertyId);
        return unitRepository.countByPropertyId(propertyId);
    }

    private void calculateMonthlyRent(UnitDTO unitDTO) {
        if (unitDTO.getRentType() == RentType.PSM &&
                unitDTO.getRatePerSqm() != null &&
                unitDTO.getSize() != null) {
            unitDTO.setMonthlyRent(unitDTO.getRatePerSqm() * unitDTO.getSize());
        }
    }

    private Unit findUnitById(Long id) {
        return unitRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Unit not found with id: " + id));
    }

    private Property getPropertyById(Long propertyId) {
        return propertyRepository.findById(propertyId)
                .orElseThrow(() -> new EntityNotFoundException("Property not found with id: " + propertyId));
    }

    private Floor getFloorIfProvided(Long floorId) {
        if (floorId == null) return null;
        return floorRepository.findById(floorId)
                .orElseThrow(() -> new EntityNotFoundException("Floor not found with id: " + floorId));
    }

    private void updateFloorOccupancyIfNeeded(Long floorId) {
        if (floorId != null) {
            floorService.updateFloorOccupancyStats(floorId);
        }
    }

    private void validatePropertyExists(Long propertyId) {
        if (!propertyRepository.existsById(propertyId)) {
            log.error("Property not found with ID: {}", propertyId);
            throw new EntityNotFoundException("Property not found with id: " + propertyId);
        }
    }

    private void validateFloorExists(Long floorId) {
        if (!floorRepository.existsById(floorId)) {
            log.error("Floor not found with ID: {}", floorId);
            throw new EntityNotFoundException("Floor not found with id: " + floorId);
        }
    }
}
