package com.tinash.propertyservice.domain.model.property;

import org.springframework.stereotype.Component;

@Component
public class PropertyMapper {
    public static PropertyDTO toDto(Property property) {
        // TODO: Implement mapping logic
        return new PropertyDTO(property.getId(), property.getName().value(), property.getManagedBy());
    }
}
