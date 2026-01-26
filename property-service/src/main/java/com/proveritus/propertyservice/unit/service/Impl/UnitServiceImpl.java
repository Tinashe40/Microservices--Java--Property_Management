package com.proveritus.propertyservice.unit.service.Impl;

import com.tinash.cloud.utility.audit.annotation.Auditable;

import com.tinash.cloud.utility.enums.OccupancyStatus;
import com.tinash.cloud.utility.enums.RentType;
import com.tinash.cloud.utility.exception.ResourceNotFoundException;
import com.tinash.cloud.utility.jpa.DomainServiceImpl;
import com.tinash.cloud.utility.security.permission.Permissions;
import com.proveritus.propertyservice.floor.domain.Floor;
import com.proveritus.propertyservice.floor.domain.FloorRepository;
import com.proveritus.propertyservice.floor.service.FloorService;
import com.proveritus.propertyservice.property.domain.Property;
import com.proveritus.propertyservice.property.domain.PropertyRepository;
import com.proveritus.propertyservice.unit.domain.Unit;
import com.proveritus.propertyservice.unit.domain.UnitRepository;
import com.proveritus.propertyservice.unit.domain.UnitValidator;
import com.proveritus.propertyservice.unit.dto.UnitDTO;
import com.proveritus.propertyservice.unit.mapper.UnitMapper;
import com.proveritus.propertyservice.unit.service.UnitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
public class UnitServiceImpl extends DomainServiceImpl<Unit, UnitDTO, UnitDTO, UnitDTO> implements UnitService {
    private final UnitRepository unitRepository;
    private final PropertyRepository propertyRepository;
    private final FloorRepository floorRepository;
    private final UnitMapper unitMapper;
    private final FloorService floorService;
    private final UnitValidator unitValidator;

    public UnitServiceImpl(UnitRepository unitRepository, PropertyRepository propertyRepository, FloorRepository floorRepository, UnitMapper unitMapper, FloorService floorService, UnitValidator unitValidator) {
        super(unitRepository, unitMapper);
        this.unitRepository = unitRepository;
        this.propertyRepository = propertyRepository;
        this.floorRepository = floorRepository;
        this.unitMapper = unitMapper;
        this.floorService = floorService;
        this.unitValidator = unitValidator;
    }

    @Override
    @CacheEvict(value = "units", allEntries = true)
    @Auditable(action = "CREATE_UNIT", entity = "Unit")
    @PreAuthorize("hasAuthority('" + Permissions.Unit.CREATE + "')")
    public UnitDTO create(UnitDTO unitDTO) {
        log.info("Creating new unit: {}", unitDTO.getName());
        unitValidator.validate(unitDTO);

        Property property = getPropertyById(unitDTO.getPropertyId());
        Floor floor = getFloorIfProvided(unitDTO.getFloorId());

        calculateMonthlyRent(unitDTO);

        Unit unit = unitMapper.fromCreateDto(unitDTO);
        unit.setProperty(property);
        unit.setFloor(floor);

        Unit savedUnit = unitRepository.save(unit);
        updateFloorOccupancyIfNeeded(unitDTO.getFloorId());

        log.debug("Unit created successfully with ID: {}", savedUnit.getId());
        return unitMapper.toDto(savedUnit);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated()")
    public UnitDTO getUnitByNameAndPropertyId(String name, String propertyId) {
        log.debug("Fetching unit with name: {} in property ID: {}", name, propertyId);
        return unitRepository.findByNameAndPropertyId(name, propertyId)
                .map(unitMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Unit not found with name: " + name + " in property ID: " + propertyId));
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated()")
    public Page<UnitDTO> getUnitsByPropertyId(String propertyId, Pageable pageable) {
        log.debug("Fetching paginated units for property ID: {}", propertyId);
        return unitRepository.findByPropertyId(propertyId, pageable)
                .map(unitMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated()")
    public Page<UnitDTO> getUnitsByFloorId(String floorId, Pageable pageable) {
        log.debug("Fetching paginated units for floor ID: {}", floorId);
        return unitRepository.findByFloorId(floorId, pageable)
                .map(unitMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated()")
    public Page<UnitDTO> getUnitsWithFilters(String propertyId, String floorId, OccupancyStatus occupancyStatus, Pageable pageable) {
        log.debug("Fetching units with filters - Property ID: {}, Floor ID: {}, Occupancy Status: {}",
                propertyId, floorId, occupancyStatus);

        return unitRepository.findWithFilters(propertyId, floorId, occupancyStatus, pageable)
                .map(unitMapper::toDto);
    }

    @Override
    @CacheEvict(value = "units", allEntries = true)
    @Auditable(action = "UPDATE_UNIT", entity = "Unit")
    @PreAuthorize("hasAuthority('" + Permissions.Unit.UPDATE + "')")
    public UnitDTO update(UnitDTO unitDTO) {
        log.info("Updating unit with ID: {}", unitDTO.getId());
        Unit existingUnit = findEntityById(unitDTO.getId());
        unitValidator.validate(unitDTO, unitDTO.getId());

        Property property = getPropertyById(unitDTO.getPropertyId());
        Floor floor = getFloorIfProvided(unitDTO.getFloorId());

        calculateMonthlyRent(unitDTO);

        unitMapper.updateFromUpdateDto(unitDTO, existingUnit);
        existingUnit.setProperty(property);
        existingUnit.setFloor(floor);

        Unit updatedUnit = unitRepository.save(existingUnit);
        updateFloorOccupancyIfNeeded(unitDTO.getFloorId());

        log.debug("Unit updated successfully with ID: {}", unitDTO.getId());
        return unitMapper.toDto(updatedUnit);
    }

    @Override
    @CacheEvict(value = "units", allEntries = true)
    @Auditable(action = "DELETE_UNIT", entity = "Unit")
    @PreAuthorize("hasAuthority('" + Permissions.Unit.DELETE + "')")
    public void deleteById(String id) {
        log.info("Deleting unit with ID: {}", id);
        Unit unit = findEntityById(id);
        String floorId = (unit.getFloor() != null) ? unit.getFloor().getId() : null;

        super.deleteById(id);
        updateFloorOccupancyIfNeeded(floorId);

        log.debug("Unit deleted successfully with ID: {}", id);
    }

    @Override
    @CacheEvict(value = "units", allEntries = true)
    @Auditable(action = "CREATE_UNITS", entity = "Unit")
    @PreAuthorize("hasAuthority('" + Permissions.Unit.CREATE + "')")
    public void createUnits(List<UnitDTO> unitDTOs) {
        log.info("Creating {} new units", unitDTOs.size());
        List<Unit> units = unitDTOs.stream()
                .map(unitDTO -> {
                    unitValidator.validate(unitDTO);
                    Property property = getPropertyById(unitDTO.getPropertyId());
                    Floor floor = getFloorIfProvided(unitDTO.getFloorId());
                    calculateMonthlyRent(unitDTO);
                    Unit unit = unitMapper.fromCreateDto(unitDTO);
                    unit.setProperty(property);
                    unit.setFloor(floor);
                    return unit;
                })
                .toList();
        unitRepository.saveAll(units);
        log.debug("Successfully created {} units", units.size());
    }

    @Override
    @CacheEvict(value = "units", allEntries = true)
    @Auditable(action = "UPDATE_UNITS", entity = "Unit")
    @PreAuthorize("hasAuthority('" + Permissions.Unit.UPDATE + "')")
    public void updateUnits(List<UnitDTO> unitDTOs) {
        log.info("Updating {} units", unitDTOs.size());
        List<Unit> units = unitDTOs.stream()
                .map(unitDTO -> {
                    Unit existingUnit = findEntityById(unitDTO.getId());
                    unitValidator.validate(unitDTO, unitDTO.getId());
                    Property property = getPropertyById(unitDTO.getPropertyId());
                    Floor floor = getFloorIfProvided(unitDTO.getFloorId());
                    calculateMonthlyRent(unitDTO);
                    unitMapper.updateFromUpdateDto(unitDTO, existingUnit);
                    existingUnit.setProperty(property);
                    existingUnit.setFloor(floor);
                    return existingUnit;
                })
                .toList();
        unitRepository.saveAll(units);
        log.debug("Successfully updated {} units", units.size());
    }

    @Override
    @CacheEvict(value = "units", allEntries = true)
    @Auditable(action = "DELETE_UNITS", entity = "Unit")
    @PreAuthorize("hasAuthority('" + Permissions.Unit.DELETE + "')")
    public void deleteUnits(List<String> ids) {
        log.info("Deleting {} units", ids.size());
        unitRepository.deleteAllById(ids);
        log.debug("Successfully deleted {} units", ids.size());
    }

    @Override
    @CacheEvict(value = "units", allEntries = true)
    @Auditable(action = "UPDATE_OCCUPANCY_STATUS", entity = "Unit")
    @PreAuthorize("hasAuthority('" + Permissions.Unit.UPDATE + "')")
    public UnitDTO updateOccupancyStatus(String id, OccupancyStatus occupancyStatus, String tenant) {
        log.info("Updating occupancy status for unit ID: {} to {}", id, occupancyStatus);
        Unit unit = findEntityById(id);

        unit.setOccupancyStatus(occupancyStatus);
        unit.setTenant(OccupancyStatus.OCCUPIED.equals(occupancyStatus) ? tenant : null);

        Unit updatedUnit = unitRepository.save(unit);
        updateFloorOccupancyIfNeeded(unit.getFloor() != null ? unit.getFloor().getId() : null);

        log.debug("Occupancy status updated successfully for unit ID: {}", id);
        return unitMapper.toDto(updatedUnit);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated()")
    public Page<UnitDTO> searchUnits(String query, Pageable pageable) {
        log.debug("Searching units by query with pagination: {}", query);
        return unitRepository.searchUnits(query, pageable)
                .map(unitMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated()")
    public Double calculatePotentialRentalIncome(String propertyId) {
        log.debug("Calculating potential rental income for property ID: {}", propertyId);
        getPropertyById(propertyId);
        return unitRepository.calculateTotalRentalIncome(propertyId);
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated()")
    public long countUnitsByPropertyId(String propertyId) {
        log.debug("Counting units for property ID: {}", propertyId);
        getPropertyById(propertyId);
        return unitRepository.countByPropertyId(propertyId);
    }

    private void calculateMonthlyRent(UnitDTO unitDTO) {
        if (unitDTO.getRentType() == RentType.PSM &&
                unitDTO.getRatePerSqm() != null &&
                unitDTO.getSize() != null) {
            unitDTO.setMonthlyRent(unitDTO.getRatePerSqm() * unitDTO.getSize());
        }
    }

    private Property getPropertyById(String propertyId) {
        return propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id: " + propertyId));
    }

    private Floor getFloorIfProvided(String floorId) {
        if (floorId == null) return null;
        return floorRepository.findById(floorId)
                .orElseThrow(() -> new ResourceNotFoundException("Floor not found with id: " + floorId));
    }

    private void updateFloorOccupancyIfNeeded(String floorId) {
        if (floorId != null) {
            floorService.updateFloorOccupancyStats(floorId);
        }
    }

    @Override
    public Class<Unit> getEntityClass() {
        return Unit.class;
    }
}