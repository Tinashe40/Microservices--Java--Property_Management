package com.proveritus.propertyservice.application.usecase.property.query;

import com.proveritus.cloudutility.core.application.Query;

public record GetPropertyQuery(String propertyId) implements Query {
}
