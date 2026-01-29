package com.tinash.propertyservice.unit.api;

import com.tinash.cloud.utility.audit.annotation.Auditable;
import com.tinash.cloud.utility.enums.OccupancyStatus;
import com.tinash.propertyservice.unit.dto.UnitDTO;
import com.tinash.propertyservice.unit.service.UnitService;
import com.tinash.propertyservice.util.PageableFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/units")
@RequiredArgsConstructor
@Tag(name = "Units", description = "APIs for managing property units")
public class UnitRestController {
    private final UnitService unitService;

    @PostMapping
    @PreAuthorize("hasAuthority(T(com.proveritus.cloudutility.security.Permissions.Unit).CREATE) and @apiFeatureControl.isEnabled('units', 'create')")
    @Operation(summary = "Create a new unit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Unit created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "Property or floor not found"),
            @ApiResponse(responseCode = "409", description = "Unit already exists")
    })
    @Auditable
    public ResponseEntity<UnitDTO> createUnit(@Valid @RequestBody UnitDTO unitDTO) {
        log.info("Creating unit: {}", unitDTO.getName());
        UnitDTO createdUnit = unitService.create(unitDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUnit);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get a unit by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Unit found"),
            @ApiResponse(responseCode = "404", description = "Unit not found")
    })
    @Auditable
    public ResponseEntity<UnitDTO> getUnitById(
            @Parameter(description = "ID of the unit to retrieve") @PathVariable Long id) {
        log.debug("Fetching unit with ID: {}", id);
        return ResponseEntity.ok(unitService.findById(id));
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get a unit by name and property ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Unit found"),
            @ApiResponse(responseCode = "404", description = "Unit not found")
    })
    @Auditable
    public ResponseEntity<UnitDTO> getUnitByName(
            @Parameter(description = "Name of the unit to retrieve") @PathVariable String name,
            @RequestParam Long propertyId) {
        log.debug("Fetching unit with name: {} in property ID: {}", name, propertyId);
        return ResponseEntity.ok(unitService.getUnitByNameAndPropertyId(name, propertyId));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get units with filtering and pagination")
    @Auditable
    public ResponseEntity<Page<UnitDTO>> getUnits(
            @RequestParam(required = false) Long propertyId,
            @RequestParam(required = false) Long floorId,
            @RequestParam(required = false) OccupancyStatus occupancyStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        log.debug("Fetching units with filters - Property ID: {}, Floor ID: {}, Occupancy: {}",
                propertyId, floorId, occupancyStatus);

        Pageable pageable = PageableFactory.createPageable(page, size, sortBy, direction);
        Page<UnitDTO> unitsPage = unitService.getUnitsWithFilters(propertyId, floorId, occupancyStatus, pageable);
        return ResponseEntity.ok(unitsPage);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.proveritus.cloudutility.security.Permissions.Unit).UPDATE) and @apiFeatureControl.isEnabled('units', 'update')")
    @Operation(summary = "Update an existing unit")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Unit updated successfully"),
            @ApiResponse(responseCode = "404", description = "Unit not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @Auditable
    public ResponseEntity<UnitDTO> updateUnit(
            @Parameter(description = "ID of the unit to update") @PathVariable Long id,
            @Valid @RequestBody UnitDTO unitDTO) {
        log.info("Updating unit with ID: {}", id);
        unitDTO.setId(id);
        return ResponseEntity.ok(unitService.update(unitDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.proveritus.cloudutility.security.Permissions.Unit).DELETE) and @apiFeatureControl.isEnabled('units', 'delete')")
    @Operation(summary = "Delete a unit by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Unit deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Unit not found")
    })
    @Auditable
    public ResponseEntity<Void> deleteUnit(
            @Parameter(description = "ID of the unit to delete") @PathVariable Long id) {
        log.info("Deleting unit with ID: {}", id);
        unitService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/bulk-create")
    @PreAuthorize("hasAuthority(T(com.proveritus.cloudutility.security.Permissions.Unit).CREATE) and @apiFeatureControl.isEnabled('units', 'bulk-create')")
    @Operation(summary = "Create multiple units")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Units created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @Auditable
    public ResponseEntity<Void> createUnits(@Valid @RequestBody List<UnitDTO> unitDTOs) {
        log.info("Creating {} units", unitDTOs.size());
        unitService.createUnits(unitDTOs);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/bulk-update")
    @PreAuthorize("hasAuthority(T(com.proveritus.cloudutility.security.Permissions.Unit).UPDATE) and @apiFeatureControl.isEnabled('units', 'bulk-update')")
    @Operation(summary = "Update multiple units")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Units updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "404", description = "One or more units not found")
    })
    public ResponseEntity<Void> updateUnits(@Valid @RequestBody List<UnitDTO> unitDTOs) {
        log.info("Updating {} units", unitDTOs.size());
        unitService.updateUnits(unitDTOs);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/bulk-delete")
    @PreAuthorize("hasAuthority(T(com.proveritus.cloudutility.security.Permissions.Unit).DELETE) and @apiFeatureControl.isEnabled('units', 'bulk-delete')")
    @Operation(summary = "Delete multiple units")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Units deleted successfully")
    })
    public ResponseEntity<Void> deleteUnits(@Parameter(description = "List of unit IDs to delete") @RequestBody List<Long> ids) {
        log.info("Deleting {} units", ids.size());
        unitService.deleteUnits(ids);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/occupancy")
    @PreAuthorize("(hasAnyAuthority(T(com.proveritus.cloudutility.security.Permissions.Unit).UPDATE, T(com.proveritus.cloudutility.security.Permissions.LeasingAgent).UPDATE_OCCUPANCY)) and @apiFeatureControl.isEnabled('units', 'update-occupancy')")
    @Operation(summary = "Update unit occupancy status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Occupancy status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Unit not found")
    })
    public ResponseEntity<UnitDTO> updateOccupancyStatus(
            @PathVariable Long id,
            @RequestParam OccupancyStatus occupancyStatus,
            @RequestParam(required = false) String tenant) {
        log.info("Updating occupancy status for unit ID: {} to {}", id, occupancyStatus);
        return ResponseEntity.ok(unitService.updateOccupancyStatus(id, occupancyStatus, tenant));
    }

    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Search units by name or tenant")
    public ResponseEntity<Page<UnitDTO>> searchUnits(
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.debug("Searching units with query: {}", query);
        Pageable pageable = PageableFactory.createPageable(page, size);
        Page<UnitDTO> unitsPage = unitService.searchUnits(query, pageable);
        return ResponseEntity.ok(unitsPage);
    }

    @GetMapping("/property/{propertyId}/income")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Calculate potential rental income for a property")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Income calculated successfully"),
            @ApiResponse(responseCode = "404", description = "Property not found")
    })
    public ResponseEntity<Double> calculateRentalIncome(
            @Parameter(description = "ID of the property") @PathVariable Long propertyId) {
        log.debug("Calculating rental income for property ID: {}", propertyId);
        return ResponseEntity.ok(unitService.calculatePotentialRentalIncome(propertyId));
    }

    @GetMapping("/property/{propertyId}/count")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Count units in a property")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Count retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Property not found")
    })
    public ResponseEntity<Long> countUnitsByProperty(
            @Parameter(description = "ID of the property") @PathVariable Long propertyId) {
        log.debug("Counting units for property ID: {}", propertyId);
        return ResponseEntity.ok(unitService.countUnitsByPropertyId(propertyId));
    }
}
