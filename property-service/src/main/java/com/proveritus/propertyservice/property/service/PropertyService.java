package com.proveritus.propertyservice.property.service;

import com.tinash.cloud.utility.enums.PropertyType;
import com.tinash.cloud.utility.exception.ResourceNotFoundException;
import com.tinash.cloud.utility.jpa.DomainService;
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
public interface PropertyService extends PropertyCommandService, PropertyQueryService {
}
