package com.proveritus.propertyservice.application.usecase.property.query;

import com.proveritus.propertyservice.domain.model.property.Property;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PropertySpecificationBuilder {
    public Specification<Property> build(Map<String, String> filter) {
        // TODO: Implement specification builder logic
        return Specification.where(null);
    }
}
