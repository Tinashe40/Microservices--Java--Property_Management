package com.proveritus.propertyservice.floor.service.Impl;

import com.proveritus.cloudutility.audit.annotation.Auditable;
import com.proveritus.cloudutility.enums.OccupancyStatus;
import com.proveritus.cloudutility.exception.ResourceNotFoundException;
import com.proveritus.cloudutility.jpa.DomainServiceImpl;
import com.proveritus.cloudutility.security.Permissions;
import com.proveritus.propertyservice.floor.domain.Floor;
import com.proveritus.propertyservice.floor.domain.FloorRepository;
import com.proveritus.propertyservice.floor.domain.FloorValidator;
import com.proveritus.propertyservice.floor.dto.FloorDTO;
import com.proveritus.propertyservice.floor.dto.FloorOccupancyStats;
import com.proveritus.propertyservice.floor.mapper.FloorMapper;
import com.proveritus.propertyservice.floor.service.FloorService;
import com.proveritus.propertyservice.property.domain.Property;
import com.proveritus.propertyservice.property.domain.PropertyRepository;
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
public class FloorServiceImpl extends DomainServiceImpl<Floor, FloorDTO, FloorDTO, FloorDTO> implements FloorService {
    private final FloorRepository floorRepository;
    private final PropertyRepository propertyRepository;
    private final FloorMapper floorMapper;
    private final FloorValidator floorValidator;

    public FloorServiceImpl(FloorRepository floorRepository, PropertyRepository propertyRepository, FloorMapper floorMapper, FloorValidator floorValidator) {
        super(floorRepository, floorMapper);
        this.floorRepository = floorRepository;
        this.propertyRepository = propertyRepository;
        this.floorMapper = floorMapper;
        this.floorValidator = floorValidator;
    }

    @Override
    @CacheEvict(value = "floors", allEntries = true)
    @Auditable(action = "CREATE_FLOOR", entity = "Floor")
    @PreAuthorize("hasAuthority('" + Permissions.Floor.CREATE + "')")
    public FloorDTO create(FloorDTO floorDTO) {
        log.info("Creating new floor: {}", floorDTO.getName());
        floorValidator.validate(floorDTO);

        Property property = validatePropertyExists(floorDTO.getPropertyId());

        Floor floor = floorMapper.fromCreateDto(floorDTO);
        floor.setProperty(property);

        Floor savedFloor = floorRepository.save(floor);
        log.debug("Floor created successfully with ID: {}", savedFloor.getId());

        return floorMapper.toDto(savedFloor);
    }

//    @Override
//    @Transactional(readOnly = true)
//    @PreAuthorize("isAuthenticated()")
//    public List<FloorDTO> getFloorsByPropertyId(Long propertyId) {
//        log.debug("Fetching all floors for property ID: {}", propertyId);
//        validatePropertyExists(propertyId);
//        return floorMapper.toDto(floorRepository.findByPropertyId(propertyId));
//    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated()")
    public Page<FloorDTO> getFloorsByPropertyId(String propertyId, Pageable pageable) {
        log.debug("Fetching paginated floors for property ID: {}", propertyId);
        validatePropertyExists(propertyId);
        return floorRepository.findByPropertyId(propertyId, pageable)
                .map(floorMapper::toDto);
    }

    @Override
    @CacheEvict(value = "floors", allEntries = true)
    @Auditable(action = "UPDATE_FLOOR", entity = "Floor")
    @PreAuthorize("hasAuthority('" + Permissions.Floor.UPDATE + "')")
    public FloorDTO update(FloorDTO floorDTO) {
        log.info("Updating floor with ID: {}", floorDTO.getId());

        Floor existingFloor = findEntityById(floorDTO.getId());
        Property property = validatePropertyExists(floorDTO.getPropertyId());

        if (!existingFloor.getProperty().getId().equals(property.getId())) {
            existingFloor.setProperty(property);
        }

        if (!existingFloor.getName().equals(floorDTO.getName())) {
            floorValidator.checkForDuplicateFloorName(floorDTO.getName(), property.getId());
        }

        floorMapper.updateFromUpdateDto(floorDTO, existingFloor);
        Floor updatedFloor = floorRepository.save(existingFloor);

        log.debug("Floor updated successfully with ID: {}", floorDTO.getId());
        return floorMapper.toDto(updatedFloor);
    }

    @Override
    @CacheEvict(value = "floors", allEntries = true)
    @Auditable(action = "DELETE_FLOOR", entity = "Floor")
    @PreAuthorize("hasAuthority('" + Permissions.Floor.DELETE + "')")
    public void deleteById(String id) {
        log.info("Deleting floor with ID: {}", id);
        Floor floor = findEntityById(id);

        if (!floor.getUnits().isEmpty()) {
            log.error("Cannot delete floor with ID: {} as it has {} units", id, floor.getUnits().size());
            throw new IllegalStateException("Cannot delete floor with existing units. Please remove units first.");
        }

        super.deleteById(id);
        log.debug("Floor deleted successfully with ID: {}", id);
    }

    @Override
    @CacheEvict(value = "floors", allEntries = true)
    @Auditable(action = "CREATE_FLOORS", entity = "Floor")
    @PreAuthorize("hasAuthority('" + Permissions.Floor.CREATE + "')")
    public void createFloors(List<FloorDTO> floorDTOs) {
        log.info("Creating {} new floors", floorDTOs.size());
        List<Floor> floors = floorDTOs.stream()
                .map(floorDTO -> {
                    floorValidator.validate(floorDTO);
                    Property property = validatePropertyExists(floorDTO.getPropertyId());
                    Floor floor = floorMapper.fromCreateDto(floorDTO);
                    floor.setProperty(property);
                    return floor;
                })
                .toList();
        floorRepository.saveAll(floors);
        log.debug("Successfully created {} floors", floors.size());
    }

    @Override
    @CacheEvict(value = "floors", allEntries = true)
    @Auditable(action = "UPDATE_FLOORS", entity = "Floor")
    @PreAuthorize("hasAuthority('" + Permissions.Floor.UPDATE + "')")
    public void updateFloors(List<FloorDTO> floorDTOs) {
        log.info("Updating {} floors", floorDTOs.size());
        List<Floor> floors = floorDTOs.stream()
                .map(floorDTO -> {
                    Floor existingFloor = findEntityById(floorDTO.getId());
                    Property property = validatePropertyExists(floorDTO.getPropertyId());

                    if (!existingFloor.getProperty().getId().equals(property.getId())) {
                        existingFloor.setProperty(property);
                    }

                    if (!existingFloor.getName().equals(floorDTO.getName())) {
                        floorValidator.checkForDuplicateFloorName(floorDTO.getName(), property.getId());
                    }

                    floorMapper.updateFromUpdateDto(floorDTO, existingFloor);
                    return existingFloor;
                })
                .toList();
        floorRepository.saveAll(floors);
        log.debug("Successfully updated {} floors", floors.size());
    }

    @Override
    @CacheEvict(value = "floors", allEntries = true)
    @Auditable(action = "DELETE_FLOORS", entity = "Floor")
    @PreAuthorize("hasAuthority('" + Permissions.Floor.DELETE + "')")
    public void deleteFloors(List<String> ids) {
        log.info("Deleting {} floors", ids.size());
        ids.forEach(id -> {
            Floor floor = findEntityById(id);

            if (!floor.getUnits().isEmpty()) {
                log.error("Cannot delete floor with ID: {} as it has {} units", id, floor.getUnits().size());
                throw new IllegalStateException("Cannot delete floor with existing units. Please remove units first.");
            }
        });
        floorRepository.deleteAllById(ids);
        log.debug("Successfully deleted {} floors", ids.size());
    }

    @Override
    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated()")
    public FloorOccupancyStats getFloorOccupancyStats(String id) {
        log.debug("Fetching occupancy stats for floor ID: {}", id);
        return floorRepository.findById(id)
                .map(this::calculateOccupancyStats)
                .orElseThrow(() -> new ResourceNotFoundException("Floor not found with id: " + id));
    }

        @Override
        @PreAuthorize("hasAuthority('" + Permissions.Floor.UPDATE + "')")
        public void updateFloorOccupancyStats(String floorId) {

            log.debug("Updating occupancy stats for floor ID: {}", floorId);
            Floor floor = findEntityById(floorId);
            long totalUnits = floor.getUnits().size();
            long occupiedUnits = floor.getUnits().stream().filter(unit -> unit.getOccupancyStatus() == OccupancyStatus.OCCUPIED).count();
            floor.setNumberOfUnits((int) totalUnits);
            floor.setOccupiedUnits((int) occupiedUnits);
            floor.setVacantUnits((int) (totalUnits - occupiedUnits));
            floorRepository.save(floor);

            log.debug("Updated occupancy stats for floor ID: {}", floorId);

        }

    

        private Property validatePropertyExists(String propertyId) {

            if (propertyId == null) {

                throw new IllegalArgumentException("Property ID must not be null");

            }

    

            return propertyRepository.findById(propertyId)

                    .orElseThrow(() -> {

                        log.error("Property not found with ID: {}", propertyId);

                        return new ResourceNotFoundException("Property not found with id: " + propertyId);

                    });

        }

    

        private FloorOccupancyStats calculateOccupancyStats(Floor floor) {

            long totalUnits = floor.getUnits().size();

            long occupiedUnits = 0;

            long vacantUnits = 0;

            long reservedUnits = 0;

            long notAvailableUnits = 0;

            long underMaintenanceUnits = 0;

    

            for (com.proveritus.propertyservice.unit.domain.Unit unit : floor.getUnits()) {

                switch (unit.getOccupancyStatus()) {

                    case OCCUPIED:

                        occupiedUnits++;

                        break;

                    case AVAILABLE:

                        vacantUnits++;

                        break;

                    case RESERVED:

                        reservedUnits++;

                        break;

                    case NOT_AVAILABLE:

                        notAvailableUnits++;

                        break;

                    case UNDER_MAINTENANCE:

                        underMaintenanceUnits++;

                        break;

                }

            }

            double occupancyRate = totalUnits > 0 ? (occupiedUnits * 100.0) / totalUnits : 0;

            double vacancyRate = totalUnits > 0 ? (vacantUnits * 100.0) / totalUnits : 0;

            double reservedRate = totalUnits > 0 ? (reservedUnits * 100.0) / totalUnits : 0;

            double notAvailableRate = totalUnits > 0 ? (notAvailableUnits * 100.0) / totalUnits : 0;
            double underMaintenanceRate = totalUnits > 0 ? (underMaintenanceUnits * 100.0) / totalUnits : 0;

            return new FloorOccupancyStats((int) totalUnits, (int) occupiedUnits, (int) vacantUnits, (int) reservedUnits,
                    (int) notAvailableUnits, (int) underMaintenanceUnits, occupancyRate, vacancyRate,
                    reservedRate, notAvailableRate, underMaintenanceRate);
        }

    @Override
    public Class<Floor> getEntityClass() {
        return Floor.class;
    }
}
