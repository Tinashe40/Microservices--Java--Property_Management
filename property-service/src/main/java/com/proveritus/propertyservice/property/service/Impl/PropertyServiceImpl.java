package com.proveritus.propertyservice.property.service.Impl;

import com.proveritus.cloudutility.audit.annotation.Auditable;
import com.proveritus.cloudutility.dto.UserDTO;
import com.proveritus.cloudutility.enums.OccupancyStatus;
import com.proveritus.cloudutility.enums.PropertyType;
import com.proveritus.cloudutility.jpa.DomainServiceImpl;
import com.proveritus.cloudutility.security.util.SecurityUtils;
import com.proveritus.propertyservice.property.domain.Property;
import com.proveritus.propertyservice.property.domain.PropertyRepository;
import com.proveritus.propertyservice.property.domain.PropertySpecification;
import com.proveritus.propertyservice.property.domain.PropertyValidator;
import com.proveritus.propertyservice.property.dto.PropertyDTO;
import com.proveritus.propertyservice.property.dto.PropertyFilterDTO;
import com.proveritus.propertyservice.property.dto.PropertyStatsDTO;
import com.proveritus.propertyservice.property.dto.SystemStatsDTO;
import com.proveritus.propertyservice.property.mapper.PropertyMapper;
import com.proveritus.propertyservice.property.repository.PropertySpecificationRepository;
import com.proveritus.propertyservice.property.service.PropertyService;
import com.proveritus.propertyservice.property.service.enrichment.UserEnrichmentService;
import com.proveritus.propertyservice.unit.domain.UnitRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class PropertyServiceImpl extends DomainServiceImpl<Property, PropertyDTO, PropertyDTO, PropertyDTO> implements PropertyService {

    private final PropertyRepository propertyRepository;
    private final PropertyMapper propertyMapper;
    private final PropertyValidator propertyValidator;
    private final UserEnrichmentService userEnrichmentService;
    private final PropertySpecificationRepository propertySpecificationRepository;

    public PropertyServiceImpl(PropertyRepository propertyRepository, PropertyMapper propertyMapper, PropertyValidator propertyValidator, UserEnrichmentService userEnrichmentService, PropertySpecificationRepository propertySpecificationRepository) {
        super(propertyRepository, propertyMapper);
        this.propertyRepository = propertyRepository;
        this.propertyMapper = propertyMapper;
        this.propertyValidator = propertyValidator;
        this.userEnrichmentService = userEnrichmentService;
        this.propertySpecificationRepository = propertySpecificationRepository;
    }

    // ========== Create Operations ==========

    @Override
    @CacheEvict(value = "properties", allEntries = true)
    @Auditable(action = "CREATE_PROPERTY", entity = "Property")
    public PropertyDTO create(PropertyDTO propertyDTO) {
        log.info("Creating new property: {}", propertyDTO.getName());
        propertyValidator.validate(propertyDTO);

        UserDTO currentUser = SecurityUtils.getCurrentUserDTO().orElseThrow(() -> new AccessDeniedException("User not authenticated"));
        propertyDTO.setManagedBy(currentUser.getId());

        Property property = propertyMapper.fromCreateDto(propertyDTO);
        Property savedProperty = propertyRepository.save(property);

        log.info("Property created successfully with ID: {}", savedProperty.getId());
        return convertToDto(savedProperty);
    }

    @Override
    @CacheEvict(value = "properties", allEntries = true)
    @Auditable(action = "CREATE_PROPERTIES", entity = "Property")
    public void createProperties(List<PropertyDTO> propertyDTOs) {
        log.info("Batch creating {} properties", propertyDTOs.size());

        UserDTO currentUser = SecurityUtils.getCurrentUserDTO().orElseThrow(() -> new AccessDeniedException("User not authenticated"));

        List<Property> properties = propertyDTOs.stream()
                .peek(dto -> {
                    propertyValidator.validate(dto);
                    dto.setManagedBy(currentUser.getId());
                })
                .map(propertyMapper::fromCreateDto)
                .collect(Collectors.toList());

        List<Property> savedProperties = propertyRepository.saveAll(properties);

        log.info("Successfully created {} properties", savedProperties.size());
    }

    // ========== Read Operations ==========

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
    @Auditable(action = "UPDATE_PROPERTY", entity = "Property")
    public PropertyDTO update(PropertyDTO propertyDTO) {
        log.info("Updating property with ID: {}", propertyDTO.getId());
        propertyValidator.validate(propertyDTO);

        Property existingProperty = findEntityById(propertyDTO.getId());

        propertyMapper.updateFromUpdateDto(propertyDTO, existingProperty);
        Property updatedProperty = propertyRepository.save(existingProperty);

        log.info("Property updated successfully: {}", propertyDTO.getId());
        return convertToDto(updatedProperty);
    }

    @Override
    @CacheEvict(value = "properties", allEntries = true)
    @Auditable(action = "UPDATE_PROPERTIES", entity = "Property")
    public void updateProperties(List<PropertyDTO> propertyDTOs) {
        log.info("Batch updating {} properties", propertyDTOs.size());

        List<Property> updatedProperties = propertyDTOs.stream()
                .peek(propertyValidator::validate)
                .map(dto -> {
                    Property property = findEntityById(dto.getId());
                    propertyMapper.updateFromUpdateDto(dto, property);
                    return property;
                })
                .collect(Collectors.toList());

        propertyRepository.saveAll(updatedProperties);

        log.info("Successfully updated {} properties", updatedProperties.size());
    }

    // ========== Delete Operations ==========

    @Override
    @CacheEvict(value = "properties", allEntries = true)
    @Auditable(action = "DELETE_PROPERTIES", entity = "Property")
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

    // ========== Private Helper Methods ==========

    private PropertyDTO convertToDto(Property property) {
        return propertyMapper.toDto(property);
    }

    private String sanitizeSearchQuery(String query) {
        if (query == null || query.trim().isEmpty()) {
            return "";
        }
        return query.replaceAll("[\"\\\\]", "").trim();
    }

    @Override
    public Class<Property> getEntityClass() {
        return Property.class;
    }
}