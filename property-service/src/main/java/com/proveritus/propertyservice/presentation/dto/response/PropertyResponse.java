package com.proveritus.propertyservice.presentation.dto.response;

public record PropertyResponse(
        String id,
        String name,
        String managedBy
) {
}
