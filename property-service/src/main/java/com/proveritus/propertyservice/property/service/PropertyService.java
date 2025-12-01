package com.proveritus.propertyservice.property.service;

import com.proveritus.cloudutility.enums.PropertyType;
import com.proveritus.cloudutility.exception.EntityNotFoundException;
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
public interface PropertyService {

    // ========== Create Operations ==========

    /**
     * Creates a new property in the system.
     *
     * @param propertyDTO the property data to create
     * @return the created property with generated ID
     */
    PropertyDTO createProperty(PropertyDTO propertyDTO);

    /**
     * Creates multiple properties in a single batch operation.
     *
     * @param propertyDTOs list of properties to create
     */
    void createProperties(List<PropertyDTO> propertyDTOs);

    // ========== Read Operations ==========

    /**
     * Retrieves a property by its ID.
     *
     * @param id the property ID
     * @return the property details
     * @throws EntityNotFoundException if property not found
     */
    PropertyDTO getPropertyById(Long id) throws EntityNotFoundException;

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
     * Updates an existing property.
     *
     * @param id the property ID to update
     * @param propertyDTO the updated property data
     * @return the updated property
     * @throws EntityNotFoundException if property not found
     */
    PropertyDTO updateProperty(Long id, PropertyDTO propertyDTO) throws EntityNotFoundException;

    /**
     * Updates multiple properties in a single batch operation.
     *
     * @param propertyDTOs list of properties to update
     */
    void updateProperties(List<PropertyDTO> propertyDTOs);

    // ========== Delete Operations ==========

    /**
     * Deletes a property by its ID.
     *
     * @param id the property ID to delete
     * @throws EntityNotFoundException if property not found
     */
    void deleteProperty(Long id) throws EntityNotFoundException;

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
     * @throws EntityNotFoundException if property not found
     */
    PropertyStatsDTO getPropertyStats(Long id) throws EntityNotFoundException;

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
