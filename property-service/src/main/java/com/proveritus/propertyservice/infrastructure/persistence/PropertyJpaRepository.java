package com.proveritus.propertyservice.infrastructure.persistence;

import com.proveritus.propertyservice.domain.model.property.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyJpaRepository extends JpaRepository<Property, String> {
    boolean existsByNameValue(String name);
    Page<Property> findByManagedBy(String managerId, Pageable pageable);
}
