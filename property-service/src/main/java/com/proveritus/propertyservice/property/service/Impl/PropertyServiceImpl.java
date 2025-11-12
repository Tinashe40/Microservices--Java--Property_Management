package com.proveritus.propertyservice.property.service.Impl;

import com.proveritus.propertyservice.property.dto.PropertyDTO;
import com.proveritus.propertyservice.property.dto.PropertyStatsDTO;
import com.proveritus.propertyservice.property.domain.Property;
import com.proveritus.cloudutility.enums.PropertyType;
import com.proveritus.cloudutility.exception.EntityNotFoundException;
import com.proveritus.propertyservice.property.domain.PropertyRepository;
import com.proveritus.propertyservice.property.service.PropertyService;
import com.proveritus.propertyservice.property.domain.PropertyValidator;
import com.proveritus.propertyservice.property.service.enrichment.UserEnrichmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proveritus.propertyservice.unit.domain.UnitRepository;
import com.proveritus.cloudutility.enums.OccupancyStatus;
import com.proveritus.propertyservice.property.dto.SystemStatsDTO;

import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PropertyServiceImpl implements PropertyService {
    private final PropertyRepository propertyRepository;
    private final UnitRepository unitRepository;
    private final ModelMapper modelMapper;
    private final PropertyValidator propertyValidator;
    private final UserEnrichmentService userEnrichmentService;

    @Override
    @CacheEvict(value = "properties", allEntries = true)
    public PropertyDTO createProperty(PropertyDTO propertyDTO) {
        log.info("Creating new property: {}", propertyDTO.getName());
        propertyValidator.validate(propertyDTO);

        Property property = modelMapper.map(propertyDTO, Property.class);

        if (property.getFloors() == null) {
            property.setFloors(new ArrayList<>());
        }
        if (property.getUnits() == null) {
            property.setUnits(new ArrayList<>());
        }

        Property savedProperty = propertyRepository.save(property);

        log.debug("Property created successfully with ID: {}", savedProperty.getId());
        return convertToDto(savedProperty);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "properties", key = "#id")
    public PropertyDTO getPropertyById(Long id) {
        log.debug("Fetching property with ID: {}", id);
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Property not found with id: " + id));
        return convertToDto(property);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PropertyDTO> getAllProperties(Pageable pageable) {
        log.debug("Fetching paginated properties");
        Page<Property> properties = propertyRepository.findAll(pageable);
        Page<PropertyDTO> propertyDTOs = properties.map(this::convertToDto);
        return userEnrichmentService.enrichPropertiesWithUserDetails(properties, propertyDTOs);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<PropertyDTO> getAllPropertiesByType(PropertyType propertyType, Pageable pageable) {
        log.debug("Fetching properties of type: {}", propertyType);
        Page<Property> properties = propertyRepository.findByPropertyType(propertyType, pageable);
        Page<PropertyDTO> propertyDTOs = properties.map(this::convertToDto);
        return userEnrichmentService.enrichPropertiesWithUserDetails(properties, propertyDTOs);
    }

    @Override
    @CacheEvict(value = "properties", allEntries = true)
    public PropertyDTO updateProperty(Long id, PropertyDTO propertyDTO) {
        log.info("Updating property with ID: {}", id);
        Property existingProperty = propertyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Property not found with id: " + id));

        if (!existingProperty.getName().equals(propertyDTO.getName()) &&
                propertyRepository.existsByName(propertyDTO.getName())) {
            throw new IllegalArgumentException("Property with name " + propertyDTO.getName() + " already exists");
        }

        modelMapper.map(propertyDTO, existingProperty);
        existingProperty.setId(id);

        Property updatedProperty = propertyRepository.save(existingProperty);
        log.debug("Property updated successfully with ID: {}", id);
        return convertToDto(updatedProperty);
    }

    @Override
    @CacheEvict(value = "properties", allEntries = true)
    public void deleteProperty(Long id) {
        log.info("Deleting property with ID: {}", id);
        if (!propertyRepository.existsById(id)) {
            log.error("Property ID: {} was not found", id);
            throw new EntityNotFoundException("Property not found with id: " + id);
        }

        Property property = propertyRepository.findById(id).get();
        if (!property.getFloors().isEmpty() || !property.getUnits().isEmpty()) {
            log.error("Cannot delete property with ID: {} as it has floors or units", id);
            throw new IllegalStateException("Cannot delete property with existing floors or units. Please remove them first.");
        }

        propertyRepository.deleteById(id);
        log.debug("Property deleted successfully with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PropertyDTO> searchProperties(String query, Pageable pageable) {
        log.debug("Searching properties by query: {}", query);
        Page<Property> properties = propertyRepository.searchProperties(query, pageable);
        Page<PropertyDTO> propertyDTOs = properties.map(this::convertToDto);
        return userEnrichmentService.enrichPropertiesWithUserDetails(properties, propertyDTOs);
    }

    @Override
    @Transactional(readOnly = true)
    public PropertyStatsDTO getPropertyStats(Long id) {
        log.debug("Fetching stats for property ID: {}", id);
        return propertyRepository.getPropertyStats(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalPropertiesCount() {
        log.debug("Fetching total properties count");
        return propertyRepository.countAllProperties();
    }

    @Override
    @Transactional(readOnly = true)
    public SystemStatsDTO getSystemWideStats() {
        log.debug("Fetching system-wide stats");

        long totalProperties = propertyRepository.count();
        long totalUnits = unitRepository.count();
        long occupiedUnits = unitRepository.countByOccupancyStatus(OccupancyStatus.OCCUPIED);
        double overallOccupancyRate = (totalUnits > 0) ? ((double) occupiedUnits / totalUnits) * 100 : 0;
        double totalActualIncome = unitRepository.calculateTotalActualIncome();
        double totalPotentialIncome = unitRepository.calculateTotalPotentialIncome();

        return new SystemStatsDTO(
                totalProperties,
                totalUnits,
                occupiedUnits,
                overallOccupancyRate,
                totalActualIncome,
                totalPotentialIncome
        );
    }

    private PropertyDTO convertToDto(Property property) {
        PropertyDTO propertyDTO = modelMapper.map(property, PropertyDTO.class);
        userEnrichmentService.enrichPropertyDTOWithUserDetails(property, propertyDTO);
        return propertyDTO;
    }
}
