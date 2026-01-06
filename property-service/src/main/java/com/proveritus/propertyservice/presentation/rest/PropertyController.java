package com.proveritus.propertyservice.presentation.rest;

import com.proveritus.propertyservice.application.usecase.property.command.CreatePropertyCommand;
import com.proveritus.propertyservice.application.usecase.property.command.CreatePropertyUseCase;
import com.proveritus.propertyservice.application.usecase.property.query.GetPropertyQuery;
import com.proveritus.propertyservice.application.usecase.property.query.GetPropertyUseCase;
import com.proveritus.propertyservice.domain.model.property.PropertyDTO;
import com.proveritus.propertyservice.presentation.dto.request.CreatePropertyRequest;
import com.proveritus.propertyservice.presentation.dto.response.PropertyResponse;
import com.proveritus.propertyservice.presentation.mapper.PropertyRequestMapper;
import com.proveritus.propertyservice.presentation.mapper.PropertyResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Property REST controller.
 * Follows Single Responsibility Principle - only handles HTTP concerns.
 */
@Slf4j
@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor 
@Tag(name = "Properties", description = "Property management endpoints")
public class PropertyController {

    private final CreatePropertyUseCase createPropertyUseCase;
    private final GetPropertyUseCase getPropertyUseCase;

    @PostMapping 
    @PreAuthorize("hasAuthority('property:create')")
    @Operation(summary = "Create a new property")
    public ResponseEntity<PropertyResponse> createProperty(
            @Valid @RequestBody CreatePropertyRequest request) {

        log.info("Creating property: {}", request.name());

        CreatePropertyCommand command = PropertyRequestMapper.toCommand(request);
        PropertyDTO result = createPropertyUseCase.execute(command);
        PropertyResponse response = PropertyResponseMapper.toResponse(result);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('property:read')")
    @Operation(summary = "Get property by ID")
    public ResponseEntity<PropertyResponse> getProperty( @PathVariable String id) {
        log.info("Fetching property: {}", id);

        GetPropertyQuery query = new GetPropertyQuery(id);
        PropertyDTO result = getPropertyUseCase.execute(query);
        PropertyResponse response = PropertyResponseMapper.toResponse(result);

        return ResponseEntity.ok(response);
    }
}
