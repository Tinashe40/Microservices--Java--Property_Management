package com.proveritus.propertyservice.application.usecase.property.query;

import com.tinash.cloud.utility.core.application.Query;

public record GetPropertyQuery(String propertyId) implements Query {
}
