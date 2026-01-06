package com.proveritus.propertyservice.application.usecase.property.command;

import com.proveritus.cloudutility.core.application.UseCase;
import com.proveritus.cloudutility.core.application.Command;
import com.proveritus.propertyservice.application.port.out.EventPublisherPort;
import com.proveritus.propertyservice.domain.model.property.*;
import com.proveritus.propertyservice.domain.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use case for creating a property.
 * Follows Single Responsibility Principle - only handles property creation.
 */
@Slf4j
@Service 
@RequiredArgsConstructor
public class CreatePropertyUseCase implements UseCase<CreatePropertyCommand, PropertyDTO> {

    private final PropertyRepository propertyRepository;
    private final EventPublisherPort eventPublisher;
    private final PropertyValidator propertyValidator;

    @Override @Transactional
    public PropertyDTO execute(CreatePropertyCommand command) {
        log.info("Creating property: {}", command.name());

        // Validate
        propertyValidator.validateForCreation(command);

        // Create domain entity using factory method
        Property property = Property.create(
                PropertyId.generate(),
                new PropertyName(command.name()),
                new Address(command.address(), command.city(), command.state(), command.postalCode()),
                command.propertyType(),
                command.managerId()
        );

        // Persist
        Property saved = propertyRepository.save(property);

        // Publish domain events
        saved.getDomainEvents().forEach(eventPublisher::publish);
        saved.clearEvents();

        log.info("Property created successfully with ID: {}", saved.getId());

        return PropertyMapper.toDto(saved);
    }
}
