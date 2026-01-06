package com.proveritus.propertyservice.application.usecase.property.query;

import com.proveritus.cloudutility.core.application.Query;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public record SearchPropertiesQuery(Map<String, String> filter, Pageable pageable) implements Query {
}
