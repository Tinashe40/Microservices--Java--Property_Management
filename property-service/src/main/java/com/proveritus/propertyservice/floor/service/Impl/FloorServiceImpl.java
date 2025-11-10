package com.proveritus.propertyservice.floor.service.Impl;

import com.proveritus.propertyservice.floor.dto.FloorDTO;
import com.proveritus.propertyservice.floor.dto.FloorOccupancyStats;
import com.proveritus.propertyservice.floor.domain.Floor;
import com.proveritus.propertyservice.floor.service.FloorService;
import com.proveritus.propertyservice.property.domain.Property;
import com.proveritus.cloudutility.exception.EntityNotFoundException;
import com.proveritus.propertyservice.floor.domain.FloorRepository;
import com.proveritus.cloudutility.enums.OccupancyStatus;
import com.proveritus.propertyservice.property.domain.PropertyRepository;
import com.proveritus.propertyservice.floor.domain.FloorValidator;
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
public class FloorServiceImpl implements FloorService {
    private final FloorRepository floorRepository;
    private final PropertyRepository propertyRepository;
    private final ModelMapper modelMapper;
    private final FloorValidator floorValidator;

    @Override
    @CacheEvict(value = "floors", allEntries = true)
    public FloorDTO createFloor(FloorDTO floorDTO) {
        log.info("Creating new floor: {}", floorDTO.getName());
        floorValidator.validate(floorDTO);

        Property property = validatePropertyExists(floorDTO.getPropertyId());
        Floor floor = modelMapper.map(floorDTO, Floor.class);
        floor.setProperty(property);

        Floor savedFloor = floorRepository.save(floor);
        log.debug("Floor created successfully with ID: {}", savedFloor.getId());

        return modelMapper.map(savedFloor, FloorDTO.class);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "floors", key = "#id")
    public FloorDTO getFloorById(Long id) {
        log.debug("Fetching floor with ID: {}", id);
        return floorRepository.findById(id)
                .map(floor -> modelMapper.map(floor, FloorDTO.class))
                .orElseThrow(() -> {
                    log.error("Floor not found with ID: {}", id);
                    return new EntityNotFoundException("Floor not found with id: " + id);
                });
    }

    @Override
    @Transactional(readOnly = true)
    public List<FloorDTO> getFloorsByPropertyId(Long propertyId) {
        log.debug("Fetching all floors for property ID: {}", propertyId);
        validatePropertyExists(propertyId);
        return floorRepository.findByPropertyId(propertyId).stream()
                .map(floor -> modelMapper.map(floor, FloorDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FloorDTO> getFloorsByPropertyId(Long propertyId, Pageable pageable) {
        log.debug("Fetching paginated floors for property ID: {}", propertyId);
        validatePropertyExists(propertyId);
        return floorRepository.findByPropertyId(propertyId, pageable)
                .map(floor -> modelMapper.map(floor, FloorDTO.class));
    }

    @Override
    @CacheEvict(value = "floors", allEntries = true)
    public FloorDTO updateFloor(Long id, FloorDTO floorDTO) {
        log.info("Updating floor with ID: {}", id);

        Floor existingFloor = floorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Floor not found with id: " + id));

        Property property = validatePropertyExists(floorDTO.getPropertyId());

        if (!existingFloor.getProperty().getId().equals(property.getId())) {
            existingFloor.setProperty(property);
        }

        if (!existingFloor.getName().equals(floorDTO.getName())) {
            floorValidator.checkForDuplicateFloorName(floorDTO.getName(), property.getId());
        }

        updateFloorFields(existingFloor, floorDTO);
        Floor updatedFloor = floorRepository.save(existingFloor);

        log.debug("Floor updated successfully with ID: {}", id);
        return modelMapper.map(updatedFloor, FloorDTO.class);
    }

    @Override
    @CacheEvict(value = "floors", allEntries = true)
    public void deleteFloor(Long id) {
        log.info("Deleting floor with ID: {}", id);
        Floor floor = floorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Floor not found with id: " + id));

        if (!floor.getUnits().isEmpty()) {
            log.error("Cannot delete floor with ID: {} as it has {} units", id, floor.getUnits().size());
            throw new IllegalStateException("Cannot delete floor with existing units. Please remove units first.");
        }

        floorRepository.deleteById(id);
        log.debug("Floor deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public FloorOccupancyStats getFloorOccupancyStats(Long id) {
        log.debug("Fetching occupancy stats for floor ID: {}", id);
        return floorRepository.findById(id)
                .map(this::calculateOccupancyStats)
                .orElseThrow(() -> new EntityNotFoundException("Floor not found with id: " + id));
    }

    @Override
    public void updateFloorOccupancyStats(Long floorId) {
        log.debug("Updating occupancy stats for floor ID: {}", floorId);
        Floor floor = floorRepository.findById(floorId)
                .orElseThrow(() -> new EntityNotFoundException("Floor not found with id: " + floorId));

        long totalUnits = floorRepository.countByPropertyId(floor.getProperty().getId());
        long occupiedUnits = floor.getUnits().stream().filter(unit -> unit.getOccupancyStatus() == OccupancyStatus.OCCUPIED).count();

        floor.setNumberOfUnits((int) totalUnits);
        floor.setOccupiedUnits((int) occupiedUnits);
        floor.setVacantUnits((int) (totalUnits - occupiedUnits));

        floorRepository.save(floor);
        log.debug("Updated occupancy stats for floor ID: {}", floorId);
    }

    private void updateFloorFields(Floor existingFloor, FloorDTO floorDTO) {
        existingFloor.setName(floorDTO.getName());
        existingFloor.setNumberOfUnits(floorDTO.getNumberOfUnits());
        existingFloor.setOccupiedUnits(floorDTO.getOccupiedUnits());
        existingFloor.setVacantUnits(floorDTO.getVacantUnits());
    }

    private Property validatePropertyExists(Long propertyId) {
        if (propertyId == null) {
            throw new IllegalArgumentException("Property ID must not be null");
        }

        return propertyRepository.findById(propertyId)
                .orElseThrow(() -> {
                    log.error("Property not found with ID: {}", propertyId);
                    return new EntityNotFoundException("Property not found with id: " + propertyId);
                });
    }

    private FloorOccupancyStats calculateOccupancyStats(Floor floor) {
        long totalUnits = floor.getUnits().size();
        long occupiedUnits = floor.getUnits().stream().filter(unit -> unit.getOccupancyStatus() == OccupancyStatus.OCCUPIED).count();
        long vacantUnits = floor.getUnits().stream().filter(unit -> unit.getOccupancyStatus() == OccupancyStatus.AVAILABLE).count();
        long reservedUnits = floor.getUnits().stream().filter(unit -> unit.getOccupancyStatus() == OccupancyStatus.RESERVED).count();
        long notAvailableUnits = floor.getUnits().stream().filter(unit -> unit.getOccupancyStatus() == OccupancyStatus.NOT_AVAILABLE).count();
        long underMaintenanceUnits = floor.getUnits().stream().filter(unit -> unit.getOccupancyStatus() == OccupancyStatus.UNDER_MAINTENANCE).count();

        double occupancyRate = totalUnits > 0 ? (occupiedUnits * 100.0) / totalUnits : 0;
        double vacancyRate = totalUnits > 0 ? (vacantUnits * 100.0) / totalUnits : 0;
        double reservedRate = totalUnits > 0 ? (reservedUnits * 100.0) / totalUnits : 0;
        double notAvailableRate = totalUnits > 0 ? (notAvailableUnits * 100.0) / totalUnits : 0;
        double underMaintenanceRate = totalUnits > 0 ? (underMaintenanceUnits * 100.0) / totalUnits : 0;

        return new FloorOccupancyStats((int) totalUnits, (int) occupiedUnits, (int) vacantUnits, (int) reservedUnits,
                (int) notAvailableUnits, (int) underMaintenanceUnits, occupancyRate, vacancyRate,
                reservedRate, notAvailableRate, underMaintenanceRate);
    }
}
