package com.proveritus.propertyservice.property.service;

import com.proveritus.cloudutility.jpa.DomainService;
import com.proveritus.propertyservice.property.domain.Property;
import com.proveritus.propertyservice.property.dto.PropertyDTO;

import java.util.List;

public interface PropertyCommandService extends DomainService<Property, PropertyDTO, PropertyDTO, PropertyDTO> {

    /**
     * Creates multiple properties in a single batch operation.
     *
     * @param propertyDTOs list of properties to create
     */
    void createProperties(List<PropertyDTO> propertyDTOs);

    /**
     * Updates multiple properties in a single batch operation.
     *
     * @param propertyDTOs list of properties to update
     */
    void updateProperties(List<PropertyDTO> propertyDTOs);

    /**
     * Deletes multiple properties in a single batch operation.
     *
     * @param ids list of property IDs to delete
     */
    void deleteProperties(List<Long> ids);
}
