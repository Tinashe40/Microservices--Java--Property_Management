package com.proveritus.propertyservice.application.usecase.property.query;

import com.tinash.cloud.utility.core.application.UseCase;
import com.proveritus.propertyservice.application.service.PropertyEnrichmentService;
import com.proveritus.propertyservice.domain.model.property.Property;
import com.proveritus.propertyservice.domain.model.property.PropertyDTO;
import com.proveritus.propertyservice.domain.model.property.PropertyMapper;
import com.proveritus.propertyservice.domain.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use case for searching properties with filters.
 * Follows SRP - only handles property search.
 */
@Service
@RequiredArgsConstructor
public class SearchPropertiesUseCase implements UseCase<SearchPropertiesQuery, Page<PropertyDTO>> {

    private final PropertyRepository propertyRepository;
    private final PropertyEnrichmentService enrichmentService;
    private final PropertySpecificationBuilder specificationBuilder;

    @Override
    @Transactional(readOnly = true)
    public Page<PropertyDTO> execute(SearchPropertiesQuery query) {
        Specification<Property> spec = specificationBuilder.build(query.filter());

        Page<Property> properties = propertyRepository.findAll(spec, query.pageable());

        Page<PropertyDTO> dtos = properties.map(PropertyMapper::toDto);

        return enrichmentService.enrichPageWithUserDetails(dtos);
    }
}
