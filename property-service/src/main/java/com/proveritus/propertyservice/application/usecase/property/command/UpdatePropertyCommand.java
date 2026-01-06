package com.proveritus.propertyservice.application.usecase.property.command;

import com.proveritus.cloudutility.core.application.Command;
import com.proveritus.propertyservice.domain.model.property.PropertyType;

public record UpdatePropertyCommand(
        String propertyId,
        String name,
        String address,
        String city,
        String state,
        String postalCode,
        PropertyType propertyType
) implements Command {
}
