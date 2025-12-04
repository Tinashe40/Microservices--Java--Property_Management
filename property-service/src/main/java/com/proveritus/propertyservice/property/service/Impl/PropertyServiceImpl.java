package com.proveritus.propertyservice.property.service.Impl;

import com.proveritus.cloudutility.dto.UserDTO;
import com.proveritus.cloudutility.enums.OccupancyStatus;
import com.proveritus.cloudutility.enums.PropertyType;
import com.proveritus.cloudutility.exception.EntityNotFoundException;
import com.proveritus.propertyservice.property.domain.Property;
import com.proveritus.propertyservice.property.domain.PropertyRepository;
import com.proveritus.propertyservice.property.domain.PropertySpecification;
import com.proveritus.propertyservice.property.domain.PropertyValidator;
import com.proveritus.propertyservice.property.dto.PropertyDTO;
import com.proveritus.propertyservice.property.dto.PropertyFilterDTO;
import com.proveritus.propertyservice.property.dto.PropertyStatsDTO;
import com.proveritus.propertyservice.property.dto.SystemStatsDTO;
import com.proveritus.propertyservice.property.repository.PropertySpecificationRepository;
import com.proveritus.propertyservice.property.service.PropertyService;
import com.proveritus.propertyservice.property.service.enrichment.UserEnrichmentService;
import com.proveritus.cloudutility.service.BaseService;
import com.proveritus.propertyservice.unit.domain.UnitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private final BaseService baseService;
    private final PropertySpecificationRepository propertySpecificationRepository;

    // ========== Create Operations ==========

    @Override
    @CacheEvict(value = "properties", allEntries = true)
    public PropertyDTO createProperty(PropertyDTO propertyDTO) {
        log.info("Creating new property: {}", propertyDTO.getName());
        propertyValidator.validate(propertyDTO);

        UserDTO currentUser = baseService.getCurrentUser();
        propertyDTO.setManagedBy(currentUser.getId());

        Property property = modelMapper.map(propertyDTO, Property.class);
        Property savedProperty = propertyRepository.save(property);

        log.info("Property created successfully with ID: {}", savedProperty.getId());
        return convertToDto(savedProperty);
    }

    @Override
    @CacheEvict(value = "properties", allEntries = true)
    public void createProperties(List<PropertyDTO> propertyDTOs) {
        log.info("Batch creating {} properties", propertyDTOs.size());

        UserDTO currentUser = baseService.getCurrentUser();

        List<Property> properties = propertyDTOs.stream()
                .peek(dto -> {
                    propertyValidator.validate(dto);
                    dto.setManagedBy(currentUser.getId());
                })
                .map(dto -> modelMapper.map(dto, Property.class))
                .collect(Collectors.toList());

        List<Property> savedProperties = propertyRepository.saveAll(properties);

        log.info("Successfully created {} properties", savedProperties.size());
    }

    // ========== Read Operations ==========

    @Override
    @Cacheable(value = "properties", key = "#id")
    @Transactional(readOnly = true)
    public PropertyDTO getPropertyById(Long id) throws EntityNotFoundException {
        log.debug("Fetching property with ID: {}", id);

        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Property not found with ID: " + id));

        return convertToDto(property);
    }

    @Override
    @Cacheable(value = "properties", key = "'all-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    @Transactional(readOnly = true)
    public Page<PropertyDTO> getAllProperties(Pageable pageable) {
        log.debug("Fetching all properties with pagination: page={}, size={}",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<Property> properties = propertyRepository.findAll(pageable);
        Page<PropertyDTO> propertyDTOs = properties.map(this::convertToDto);

        return userEnrichmentService.enrichPropertiesWithUserDetails(properties, propertyDTOs);
    }

    @Override
    @Cacheable(value = "properties", key = "'type-' + #propertyType + '-' + #pageable.pageNumber")
    @Transactional(readOnly = true)
    public Page<PropertyDTO> getAllPropertiesByType(PropertyType propertyType, Pageable pageable) {
        log.debug("Fetching properties by type: {}", propertyType);

        Page<Property> properties = propertyRepository.findByPropertyType(propertyType, pageable);
        Page<PropertyDTO> propertyDTOs = properties.map(this::convertToDto);

        return userEnrichmentService.enrichPropertiesWithUserDetails(properties, propertyDTOs);
    }

    @Override
    @Cacheable(value = "properties", key = "'manager-' + #managerId + '-' + #pageable.pageNumber")
    @Transactional(readOnly = true)
    public Page<PropertyDTO> getPropertiesByManager(Long managerId, Pageable pageable) {
        log.debug("Fetching properties for manager ID: {}", managerId);

        Page<Property> properties = propertyRepository.findByManagedBy(managerId, pageable);
        Page<PropertyDTO> propertyDTOs = properties.map(this::convertToDto);

        return userEnrichmentService.enrichPropertiesWithUserDetails(properties, propertyDTOs);
    }

    // ========== Update Operations ==========

    @Override
    @CacheEvict(value = "properties", allEntries = true)
    public PropertyDTO updateProperty(Long id, PropertyDTO propertyDTO) throws EntityNotFoundException {
        log.info("Updating property with ID: {}", id);
        propertyValidator.validate(propertyDTO);

        Property existingProperty = propertyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Property not found with ID: " + id));

        modelMapper.map(propertyDTO, existingProperty);
        Property updatedProperty = propertyRepository.save(existingProperty);

        log.info("Property updated successfully: {}", id);
        return convertToDto(updatedProperty);
    }

    @Override
    @CacheEvict(value = "properties", allEntries = true)
    public void updateProperties(List<PropertyDTO> propertyDTOs) {
        log.info("Batch updating {} properties", propertyDTOs.size());

        List<Property> updatedProperties = propertyDTOs.stream()
                .peek(propertyValidator::validate)
                .map(dto -> {
                    Property property = propertyRepository.findById(dto.getId())
                            .orElseThrow(() -> new EntityNotFoundException(
                                    "Property not found with ID: " + dto.getId()));
                    modelMapper.map(dto, property);
                    return property;
                })
                .collect(Collectors.toList());

        propertyRepository.saveAll(updatedProperties);

        log.info("Successfully updated {} properties", updatedProperties.size());
    }

    // ========== Delete Operations ==========

    @Override
    @CacheEvict(value = "properties", allEntries = true)
    public void deleteProperty(Long id) throws EntityNotFoundException {
        log.info("Deleting property with ID: {}", id);

        if (!propertyRepository.existsById(id)) {
            throw new EntityNotFoundException("Property not found with ID: " + id);
        }

        propertyRepository.deleteById(id);

        log.info("Property deleted successfully: {}", id);
    }

    @Override
    @CacheEvict(value = "properties", allEntries = true)
    public void deleteProperties(List<Long> ids) {
        log.info("Batch deleting {} properties", ids.size());

        List<Long> existingIds = ids.stream()
                .filter(propertyRepository::existsById)
                .collect(Collectors.toList());

        if (existingIds.isEmpty()) {
            log.warn("No valid property IDs found for deletion");
            return;
        }

        propertyRepository.deleteAllById(existingIds);

        log.info("Successfully deleted {} properties", existingIds.size());
    }

    // ========== Search Operations ==========

    @Override
    @Transactional(readOnly = true)
    public Page<PropertyDTO> searchProperties(PropertyFilterDTO filter, Pageable pageable) {
        log.debug("Executing specification search with filter: {}", filter);

        Specification<Property> spec = PropertySpecification.withDynamicQuery(
                filter.getName(),
                filter.getAddress(),
                filter.getPropertyType(),
                filter.getMinFloors(),
                filter.getMaxFloors(),
                filter.getMinUnits(),
                filter.getMaxUnits()
        );

        Page<Property> properties = propertySpecificationRepository.findAll(spec, pageable);
        Page<PropertyDTO> propertyDTOs = properties.map(this::convertToDto);

        return userEnrichmentService.enrichPropertiesWithUserDetails(properties, propertyDTOs);
    }

    // ========== Statistics Operations ==========

    @Override
    @Cacheable(value = "propertyStats", key = "#id")
    @Transactional(readOnly = true)
    public PropertyStatsDTO getPropertyStats(Long id) {
        log.debug("Fetching statistics for property ID: {}", id);

        PropertyStatsDTO stats = propertyRepository.getPropertyStats(id);

        if (stats == null) {
            return new PropertyStatsDTO(0, 0, 0, 0, 0, 0, 0, 0.0, 0.0, 0.0, 0.0);
        }

        return stats;
    }

    @Override
    @PreAuthorize("hasAuthority('property:count:read')")
    @Transactional(readOnly = true)
    public long getTotalPropertiesCount() {
        log.debug("Fetching total properties count");
        return propertyRepository.countAllProperties();
    }

    @Override
    @PreAuthorize("hasAuthority('property:count:read')")
    @Transactional(readOnly = true)
    public long countPropertiesByType(PropertyType propertyType) {
        log.debug("Counting properties by type: {}", propertyType);
        return propertyRepository.countByPropertyType(propertyType);
    }

    @Override
    @PreAuthorize("hasAuthority('system:stats:read')")
    @Cacheable(value = "systemStats", key = "'all'")
    @Transactional(readOnly = true)
    public SystemStatsDTO getSystemWideStats() {
        log.debug("Fetching system-wide statistics");
        return calculateSystemStats(null);
    }

    @Override
    @PreAuthorize("hasAuthority('system:stats:read')")
    @Cacheable(value = "systemStats", key = "#propertyType")
    @Transactional(readOnly = true)
    public SystemStatsDTO getSystemWideStatsByType(PropertyType propertyType) {
        log.debug("Fetching system-wide statistics for property type: {}", propertyType);
        return calculateSystemStats(propertyType);
    }

    @Override
    @Transactional(readOnly = true)
    public PropertyStatsDTO getPropertyStatsByManager(Long managerId) {
        log.debug("Fetching statistics for properties managed by manager ID: {}", managerId);
        PropertyStatsDTO stats = propertyRepository.getPropertyStatsByManager(managerId);
        if (stats == null) {
            return new PropertyStatsDTO(0, 0, 0, 0, 0, 0, 0, 0.0, 0.0, 0.0, 0.0);
        }
        return stats;
    }

    // ========== Private Helper Methods ==========

    private PropertyDTO convertToDto(Property property) {
        PropertyDTO propertyDTO = modelMapper.map(property, PropertyDTO.class);
        userEnrichmentService.enrichPropertyDTOWithUserDetails(property, propertyDTO);
        return propertyDTO;
    }

    private String sanitizeSearchQuery(String query) {
        if (query == null || query.trim().isEmpty()) {
            return "";
        }
        return query.replaceAll("[\"\\\\]", "").trim();
    }

    private SystemStatsDTO calculateSystemStats(PropertyType propertyType) {
        long totalProperties = propertyType == null
                ? propertyRepository.count()
                : propertyRepository.countByPropertyType(propertyType);

        long totalUnits = unitRepository.count();
        long occupiedUnits = unitRepository.countByOccupancyStatus(OccupancyStatus.OCCUPIED);

        double occupancyRate = totalUnits > 0
                ? ((double) occupiedUnits / totalUnits) * 100
                : 0.0;

        double totalActualIncome = Optional.ofNullable(unitRepository.calculateTotalActualIncome()).orElse(0.0);
        double totalPotentialIncome = Optional.ofNullable(unitRepository.calculateTotalPotentialIncome()).orElse(0.0);

        return new SystemStatsDTO(
                totalProperties,
                totalUnits,
                occupiedUnits,
                occupancyRate,
                totalActualIncome,
                totalPotentialIncome
        );
    }
}