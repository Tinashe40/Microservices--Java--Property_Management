package com.tinash.propertyservice.property.service;

import com.tinash.cloud.utility.exception.ResourceNotFoundException;
import com.tinash.cloud.utility.jpa.DomainService;

/**
 * Service interface for property management operations.
 * Provides CRUD operations, search functionality, and statistical reporting.
 */
public interface PropertyService extends PropertyCommandService, PropertyQueryService {
}
