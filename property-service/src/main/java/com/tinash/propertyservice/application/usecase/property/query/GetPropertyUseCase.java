package com.tinash.propertyservice.application.usecase.property.query;

import com.tinash.cloud.utility.core.application.UseCase;
import com.tinash.propertyservice.application.service.PropertyEnrichmentService;
import com.tinash.propertyservice.domain.model.property.Property;
import com.tinash.propertyservice.domain.model.property.PropertyDTO;
import com.tinash.propertyservice.domain.model.property.PropertyId;
import com.tinash.propertyservice.domain.model.property.PropertyMapper;
import com.tinash.propertyservice.domain.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GetPropertyUseCase implements UseCase<GetPropertyQuery, PropertyDTO> {

    private final PropertyRepository propertyRepository;
    private final PropertyEnrichmentService enrichmentService;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "properties", key = "#query.propertyId()")
    public PropertyDTO execute(GetPropertyQuery query) {
        Property property = propertyRepository.findById(new PropertyId(query.propertyId()))
                .orElseThrow(() -> new RuntimeException("Property not found")); // Replace with specific exception

        PropertyDTO dto = PropertyMapper.toDto(property);

        // Enrich with user details
        return enrichmentService.enrichWithUserDetails(dto);
    }
}
