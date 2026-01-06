package com.proveritus.propertyservice.infrastructure.persistence;

import com.proveritus.propertyservice.domain.model.property.Property;
import com.proveritus.propertyservice.domain.model.property.PropertyId;
import com.proveritus.propertyservice.domain.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * JPA repository adapter.
 * Implements domain repository interface.
 * Follows Dependency Inversion Principle.
 */
@Component 
@RequiredArgsConstructor
public class PropertyRepositoryAdapter implements PropertyRepository {

    private final PropertyJpaRepository jpaRepository;

    @Override
    public Property save(Property property) {
        return jpaRepository.save(property);
    }

    @Override
    public Optional<Property> findById(PropertyId id) {
        return jpaRepository.findById(id.value());
    }

    @Override
    public Page<Property> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable);
    }

    @Override
    public void delete(PropertyId id) {
        jpaRepository.deleteById(id.value());
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByNameValue(name);
    }

    @Override
    public Page<Property> findByManagedBy(String managerId, Pageable pageable) {
        return jpaRepository.findByManagedBy(managerId, pageable);
    }
}
