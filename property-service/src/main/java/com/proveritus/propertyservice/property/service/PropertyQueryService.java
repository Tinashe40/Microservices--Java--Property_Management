package com.proveritus.propertyservice.property.service;

import com.proveritus.cloudutility.enums.PropertyType;
import com.proveritus.propertyservice.property.dto.PropertyDTO;
import com.proveritus.propertyservice.property.dto.PropertyFilterDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PropertyQueryService {

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

    /**
     * Searches properties using advanced filtering criteria.
     *
     * @param filter the filter criteria
     * @param pageable pagination parameters
     * @return paginated filtered results
     */
    Page<PropertyDTO> searchProperties(PropertyFilterDTO filter, Pageable pageable);
}
