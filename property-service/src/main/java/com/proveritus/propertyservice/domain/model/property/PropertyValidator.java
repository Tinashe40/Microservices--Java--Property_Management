package com.proveritus.propertyservice.domain.model.property;

import com.proveritus.propertyservice.application.usecase.property.command.CreatePropertyCommand;
import com.proveritus.propertyservice.application.usecase.property.command.UpdatePropertyCommand;
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
