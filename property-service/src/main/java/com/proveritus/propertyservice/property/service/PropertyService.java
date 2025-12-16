package com.proveritus.propertyservice.property.service;

import com.proveritus.cloudutility.enums.PropertyType;
import com.proveritus.cloudutility.exception.ResourceNotFoundException;
import com.proveritus.cloudutility.jpa.DomainService;
import com.proveritus.propertyservice.property.domain.Property;
import com.proveritus.propertyservice.property.dto.PropertyDTO;
import com.proveritus.propertyservice.property.dto.PropertyFilterDTO;
import com.proveritus.propertyservice.property.dto.PropertyStatsDTO;
import com.proveritus.propertyservice.property.dto.SystemStatsDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface for property management operations.
 * Provides CRUD operations, search functionality, and statistical reporting.
 */
public interface PropertyService extends DomainService<Property, PropertyDTO, PropertyDTO, PropertyDTO> {

    // ========== Create Operations ==========

    /**
     * Creates multiple properties in a single batch operation.
     *
     * @param propertyDTOs list of properties to create
     */
    void createProperties(List<PropertyDTO> propertyDTOs);

    // ========== Read Operations ==========

    /**
     * Retrieves all properties with pagination.
     *
     * @param pageable pagination parameters
     * @return paginated list of properties
     */
    Page<PropertyDTO> getAllProperties(Pageable pageable);

    /**
     * Retrieves properties filtered by type.
     *
     * @param propertyType the type of property to filter by
     * @param pageable pagination parameters
     * @return paginated list of properties of the specified type
     */
    Page<PropertyDTO> getAllPropertiesByType(PropertyType propertyType, Pageable pageable);

    /**
     * Retrieves properties managed by a specific manager.
     *
     * @param managerId the ID of the property manager
     * @param pageable pagination parameters
     * @return paginated list of properties managed by the specified manager
     */
    Page<PropertyDTO> getPropertiesByManager(Long managerId, Pageable pageable);

    // ========== Update Operations ==========

    /**
     * Updates multiple properties in a single batch operation.
     *
     * @param propertyDTOs list of properties to update
     */
    void updateProperties(List<PropertyDTO> propertyDTOs);

    // ========== Delete Operations ==========

    /**
     * Deletes multiple properties in a single batch operation.
     *
     * @param ids list of property IDs to delete
     */
    void deleteProperties(List<Long> ids);

    // ========== Search Operations ==========

    /**
     * Searches properties using advanced filtering criteria.
     *
     * @param filter the filter criteria
     * @param pageable pagination parameters
     * @return paginated filtered results
     */
    Page<PropertyDTO> searchProperties(PropertyFilterDTO filter, Pageable pageable);

    // ========== Statistics Operations ==========

    /**
     * Retrieves statistics for a specific property.
     *
     * @param id the property ID
     * @return property statistics including occupancy and financial data
     * @throws ResourceNotFoundException if property not found
     */
    PropertyStatsDTO getPropertyStats(Long id) throws ResourceNotFoundException;

    /**
     * Gets the total count of all properties in the system.
     * Requires ADMIN or SUPER_ADMIN role.
     *
     * @return total number of properties
     */
    long getTotalPropertiesCount();

    /**
     * Gets the count of properties by type.
     * Requires ADMIN or SUPER_ADMIN role.
     *
     * @param propertyType the property type to count
     * @return number of properties of the specified type
     */
    long countPropertiesByType(PropertyType propertyType);

    /**
     * Retrieves system-wide statistics across all properties.
     * Requires ADMIN or SUPER_ADMIN role.
     *
     * @return system-wide statistics
     */
    SystemStatsDTO getSystemWideStats();

    /**
     * Retrieves system-wide statistics filtered by property type.
     * Requires ADMIN or SUPER_ADMIN role.
     *
     * @param propertyType the property type to filter by
     * @return system-wide statistics for the specified type
     */
    SystemStatsDTO getSystemWideStatsByType(PropertyType propertyType);

    /**
     * Retrieves statistics for all properties managed by a specific manager.
     *
     * @param managerId the ID of the property manager
     * @return aggregated statistics for the manager's properties
     */
    PropertyStatsDTO getPropertyStatsByManager(Long managerId);
}
