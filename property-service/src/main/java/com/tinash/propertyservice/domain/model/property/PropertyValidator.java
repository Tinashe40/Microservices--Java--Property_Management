package com.tinash.propertyservice.domain.model.property;

import com.tinash.propertyservice.application.usecase.property.command.CreatePropertyCommand;
import com.tinash.propertyservice.application.usecase.property.command.UpdatePropertyCommand;
import org.springframework.stereotype.Component;

@Component
public class PropertyValidator {
    public void validateForCreation(CreatePropertyCommand command) {
        // TODO: Add validation logic
    }

    public void validateForUpdate(UpdatePropertyCommand command, Property property) {
        // TODO: Add validation logic
    }
}
