package com.proveritus.propertyservice.application.usecase.property.command;

import com.tinash.cloud.utility.core.application.Command;
import com.proveritus.propertyservice.domain.model.property.PropertyType;

public record CreatePropertyCommand(
        String name,
        String address,
        String city,
        String state,
        String postalCode,
        PropertyType propertyType,
        String managerId
) implements Command {
}
