package com.tinash.propertyservice.property.service;

import com.tinash.cloud.utility.enums.PropertyType;
import com.tinash.cloud.utility.exception.ResourceNotFoundException;
import com.tinash.propertyservice.property.dto.PropertyStatsDTO;
import com.tinash.propertyservice.property.dto.SystemStatsDTO;

public interface PropertyStatisticsService {

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
