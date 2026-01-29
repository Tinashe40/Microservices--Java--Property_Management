package com.tinash.propertyservice.property.repository;

import com.tinash.propertyservice.property.domain.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertySpecificationRepository extends JpaRepository<Property, Long>, JpaSpecificationExecutor<Property> {
}
