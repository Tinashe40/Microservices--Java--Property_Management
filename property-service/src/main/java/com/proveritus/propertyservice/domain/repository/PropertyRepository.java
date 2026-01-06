package com.proveritus.propertyservice.domain.repository;

import com.proveritus.propertyservice.domain.model.property.Property;
import com.proveritus.propertyservice.domain.model.property.PropertyId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Property repository port.
 * Defined in domain layer, implemented in infrastructure layer.
 */
public interface PropertyRepository {
    
    Property save(Property property);
    
    Optional<Property> findById(PropertyId id);
    
    Page<Property> findAll(Pageable pageable);
    
    void delete(PropertyId id);
    
    boolean existsByName(String name);
    
    Page<Property> findByManagedBy(String managerId, Pageable pageable);
}
