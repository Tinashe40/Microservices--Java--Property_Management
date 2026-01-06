package com.proveritus.propertyservice.application.usecase.property.command;

import com.proveritus.cloudutility.core.application.UseCase;
import com.proveritus.propertyservice.application.port.out.EventPublisherPort;
import com.proveritus.propertyservice.domain.model.property.*;
import com.proveritus.propertyservice.domain.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UpdatePropertyUseCase implements UseCase<UpdatePropertyCommand, PropertyDTO> {

    private final PropertyRepository propertyRepository;
    private final EventPublisherPort eventPublisher;
    private final PropertyValidator propertyValidator;

    @Override
    @Transactional
    public PropertyDTO execute(UpdatePropertyCommand command) {
        log.info("Updating property: {}", command.propertyId());

        // Find existing property
        Property property = propertyRepository.findById(new PropertyId(command.propertyId()))
                .orElseThrow(() -> new RuntimeException("Property not found")); // Replace with specific exception

        // Validate
        propertyValidator.validateForUpdate(command, property);

        // Update using domain methods
        property.updateDetails(
                new PropertyName(command.name()),
                new Address(command.address(), command.city(), command.state(), command.postalCode()),
                command.propertyType()
        );

        // Persist
        Property updated = propertyRepository.save(property);

        // Publish events
        updated.getDomainEvents().forEach(eventPublisher::publish);
        updated.clearEvents();

        log.info("Property updated successfully: {}", command.propertyId());

        return PropertyMapper.toDto(updated);
    }
}
