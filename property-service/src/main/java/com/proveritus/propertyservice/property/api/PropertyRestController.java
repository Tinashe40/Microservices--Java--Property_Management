package com.proveritus.propertyservice.property.api;

import com.proveritus.cloudutility.audit.annotation.Auditable;

import com.proveritus.cloudutility.enums.PropertyType;

import com.proveritus.propertyservice.property.dto.PropertyDTO;

import com.proveritus.propertyservice.property.dto.PropertyFilterDTO;

import com.proveritus.propertyservice.property.dto.PropertyStatsDTO;

import com.proveritus.propertyservice.property.dto.SystemStatsDTO;

import com.proveritus.propertyservice.property.service.PropertyCommandService;

import com.proveritus.propertyservice.property.service.PropertyQueryService;

import com.proveritus.propertyservice.property.service.PropertyStatisticsService;

import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.Parameter;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

import io.swagger.v3.oas.annotations.responses.ApiResponses;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
@Tag(name = "Properties", description = "APIs for managing properties")
@SecurityRequirement(name = "bearerAuth")
public class PropertyRestController {
    private final PropertyCommandService propertyCommandService;
    private final PropertyQueryService propertyQueryService;
    private final PropertyStatisticsService propertyStatisticsService;

    @Auditable
    @PostMapping
    @PreAuthorize("hasAuthority(T(com.proveritus.cloudutility.security.Permissions.Property).CREATE)")
    @Operation(summary = "Create a new property")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Property created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Property already exists")
    })
    public ResponseEntity<PropertyDTO> createProperty(@Valid @RequestBody PropertyDTO propertyDTO) {
        log.info("Creating property: {}", propertyDTO.getName());
        PropertyDTO createdProperty = propertyCommandService.create(propertyDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProperty);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority(T(com.proveritus.cloudutility.security.Permissions.Property).READ, T(com.proveritus.cloudutility.security.Permissions.LeasingAgent).READ, T(com.proveritus.cloudutility.security.Permissions.MaintenanceStaff).READ)")
    @Operation(summary = "Get a property by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Property found"),
            @ApiResponse(responseCode = "404", description = "Property not found")
    })
    public ResponseEntity<PropertyDTO> getPropertyById(
            @Parameter(description = "ID of the property to retrieve") @PathVariable Long id) {
        log.debug("Fetching property with ID: {}", id);
        return ResponseEntity.ok(propertyCommandService.findById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority(T(com.proveritus.cloudutility.security.Permissions.Property).READ, T(com.proveritus.cloudutility.security.Permissions.LeasingAgent).READ, T(com.proveritus.cloudutility.security.Permissions.MaintenanceStaff).READ)")
    @Operation(summary = "Search properties with optional filtering and pagination")
    public ResponseEntity<Page<PropertyDTO>> searchProperties(
            @Parameter(description = "Filter criteria for properties") PropertyFilterDTO filter,
            Pageable pageable) {
        log.debug("Searching properties with filter: {}, pageable: {}", filter, pageable);
        return ResponseEntity.ok(propertyQueryService.searchProperties(filter, pageable));
    }

    @GetMapping("/by-manager/{managerId}")
    @PreAuthorize("hasAuthority(T(com.proveritus.cloudutility.security.Permissions.Property).READ)")
    @Operation(summary = "Get properties by manager ID")
    public ResponseEntity<Page<PropertyDTO>> getPropertiesByManager(
            @PathVariable Long managerId,
            Pageable pageable) {

        log.debug("Fetching properties with managerId: {}, pageable: {}", managerId, pageable);
        return ResponseEntity.ok(propertyQueryService.getPropertiesByManager(managerId, pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.proveritus.cloudutility.security.Permissions.Property).UPDATE)")
    @Operation(summary = "Update an existing property")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Property updated successfully"),
            @ApiResponse(responseCode = "404", description = "Property not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Property name already exists")
    })
    public ResponseEntity<PropertyDTO> updateProperty(
            @Parameter(description = "ID of the property to update") @PathVariable Long id,
            @Valid @RequestBody PropertyDTO propertyDTO) {
        log.info("Updating property with ID: {}", id);
        propertyDTO.setId(id);
        return ResponseEntity.ok(propertyCommandService.update(propertyDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.proveritus.cloudutility.security.Permissions.Property).DELETE)")
    @Operation(summary = "Delete a property by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Property deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Property not found"),
            @ApiResponse(responseCode = "409", description = "Property has floors/units and cannot be deleted")
    })
    public ResponseEntity<Void> deleteProperty(
            @Parameter(description = "ID of the property to delete") @PathVariable Long id) {
        log.info("Deleting property with ID: {}", id);
        propertyCommandService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/stats")
    @PreAuthorize("hasAuthority(T(com.proveritus.cloudutility.security.Permissions.Property).READ)")
    @Operation(summary = "Get statistics for a property")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Property not found")
    })
    public ResponseEntity<PropertyStatsDTO> getPropertyStats(
            @Parameter(description = "ID of the property") @PathVariable Long id) {
        log.debug("Fetching stats for property ID: {}", id);
        return ResponseEntity.ok(propertyStatisticsService.getPropertyStats(id));
    }

    @GetMapping("/count")
    @PreAuthorize("hasAuthority(T(com.proveritus.cloudutility.security.Permissions.Property).COUNT_READ)")
    @Operation(summary = "Get total number of properties")
    public ResponseEntity<Long> getPropertiesCount() {
        log.debug("Fetching properties count");
        return ResponseEntity.ok(propertyStatisticsService.getTotalPropertiesCount());
    }

    @GetMapping("/stats/system-wide")
    @PreAuthorize("hasAuthority(T(com.proveritus.cloudutility.security.Permissions.System).STATS_READ)")
    @Operation(summary = "Get system-wide statistics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully")
    })
    public ResponseEntity<SystemStatsDTO> getSystemWideStats() {
        log.debug("Fetching system-wide stats");
        return ResponseEntity.ok(propertyStatisticsService.getSystemWideStats());
    }

    @GetMapping("/count-by-type")
    @PreAuthorize("hasAuthority(T(com.proveritus.cloudutility.security.Permissions.Property).COUNT_READ)")
    @Operation(summary = "Get total number of properties by type")
    public ResponseEntity<Long> getPropertiesCountByType(@RequestParam PropertyType propertyType) {
        log.debug("Fetching properties count by type: {}", propertyType);
        return ResponseEntity.ok(propertyStatisticsService.countPropertiesByType(propertyType));
    }

    @GetMapping("/stats/system-wide-by-type")
    @PreAuthorize("hasAuthority(T(com.proveritus.cloudutility.security.Permissions.System).STATS_READ)")
    @Operation(summary = "Get system-wide statistics by property type")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully")
    })
    public ResponseEntity<SystemStatsDTO> getSystemWideStatsByType(@RequestParam PropertyType propertyType) {
        log.debug("Fetching system-wide stats by type: {}", propertyType);
        return ResponseEntity.ok(propertyStatisticsService.getSystemWideStatsByType(propertyType));
    }

    @GetMapping("/by-manager/{managerId}/stats")
    @PreAuthorize("hasAuthority(T(com.proveritus.cloudutility.security.Permissions.Property).READ)")
    @Operation(summary = "Get statistics for properties by manager ID")
    public ResponseEntity<PropertyStatsDTO> getPropertyStatsByManager(
            @PathVariable Long managerId) {
        log.debug("Fetching stats for properties with managerId: {}", managerId);
        return ResponseEntity.ok(propertyStatisticsService.getPropertyStatsByManager(managerId));
    }
}
