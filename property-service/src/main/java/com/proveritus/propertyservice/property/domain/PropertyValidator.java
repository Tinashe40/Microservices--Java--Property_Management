package com.proveritus.propertyservice.property.domain;

import com.proveritus.propertyservice.property.dto.PropertyDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PropertyValidator {

    private final PropertyRepository propertyRepository;

    public void validate(PropertyDTO propertyDTO) {
        Optional<Property> existingProperty = propertyRepository.findByName(propertyDTO.getName());

        if (existingProperty.isPresent() && (propertyDTO.getId() == null || !existingProperty.get().getId().equals(propertyDTO.getId()))) {
            throw new IllegalArgumentException("Property with name " + propertyDTO.getName() + " already exists");
        }

        if (propertyDTO.getName() == null || propertyDTO.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Property name cannot be empty");
        }

        if (propertyDTO.getAddress() == null || propertyDTO.getAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("Property address cannot be empty");
        }

        if (propertyDTO.getPropertyType() == null) {
            throw new IllegalArgumentException("Property type cannot be null");
        }
    }
}
