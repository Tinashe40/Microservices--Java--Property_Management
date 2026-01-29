package com.tinash.propertyservice.presentation.dto.request;

import com.tinash.propertyservice.domain.model.property.PropertyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreatePropertyRequest(
        @NotBlank String name,
        @NotBlank String address,
        String city,
        String state,
        String postalCode,
        @NotNull PropertyType propertyType,
        @NotBlank String managerId
) {
}
