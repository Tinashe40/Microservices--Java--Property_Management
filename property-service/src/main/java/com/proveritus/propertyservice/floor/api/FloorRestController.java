package com.proveritus.propertyservice.floor.api;

import com.tinash.cloud.utility.audit.annotation.Auditable;

import com.proveritus.propertyservice.floor.dto.*;
import com.proveritus.propertyservice.floor.service.*;
import com.proveritus.propertyservice.util.PageableFactory;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.responses.*;
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

@RequiredArgsConstructor
@RequestMapping("/api/floors")
@Tag(name = "Floors", description = "APIs for managing building floors")
public class FloorRestController {
    private final FloorService floorService;

    @PostMapping
    @PreAuthorize("hasAuthority(T(com.proveritus.cloudutility.security.Permissions.Floor).CREATE) and @apiFeatureControl.isEnabled('floors', 'create')")
    @Operation(summary = "Create a new floor")
    @Auditable
    public ResponseEntity<FloorDTO> createFloor(@Valid @RequestBody FloorDTO floorDTO) {
        log.info("Creating floor for property ID: {}", floorDTO.getPropertyId());
        FloorDTO createdFloor = floorService.create(floorDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFloor);
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get a floor by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Floor found"),
            @ApiResponse(responseCode = "404", description = "Floor not found")
    })
    @Auditable
    public ResponseEntity<FloorDTO> getFloorById(
            @Parameter(description = "ID of the floor to retrieve") @PathVariable Long id) {
        log.debug("Fetching floor with ID: {}", id);
        return ResponseEntity.ok(floorService.findById(id));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get floors by property ID with optional pagination")
    @Auditable
    public ResponseEntity<Page<FloorDTO>> getFloorsByPropertyId(
            @RequestParam Long propertyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {

        log.debug("Fetching floors for property ID: {}, page: {}, size: {}", propertyId, page, size);
        Pageable pageable = PageableFactory.createPageable(page, size, sortBy, direction);
        Page<FloorDTO> floorsPage = floorService.getFloorsByPropertyId(propertyId, pageable);
        return ResponseEntity.ok(floorsPage);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.proveritus.cloudutility.security.Permissions.Floor).UPDATE) and @apiFeatureControl.isEnabled('floors', 'update')")
    @Operation(summary = "Update an existing floor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Floor updated successfully"),
            @ApiResponse(responseCode = "404", description = "Floor not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    @Auditable
    public ResponseEntity<FloorDTO> updateFloor(
            @Parameter(description = "ID of the floor to update") @PathVariable Long id,
            @Valid @RequestBody FloorDTO floorDTO) {
        log.info("Updating floor with ID: {}", id);
        floorDTO.setId(id);
        return ResponseEntity.ok(floorService.update(floorDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority(T(com.proveritus.cloudutility.security.Permissions.Floor).DELETE) and @apiFeatureControl.isEnabled('floors', 'delete')")
    @Operation(summary = "Delete a floor by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Floor deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Floor not found"),
            @ApiResponse(responseCode = "409", description = "Floor has units and cannot be deleted")
    })
    @Auditable
    public ResponseEntity<Void> deleteFloor(
            @Parameter(description = "ID of the floor to delete") @PathVariable Long id) {
        log.info("Deleting floor with ID: {}", id);
        floorService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/bulk-create")
    @PreAuthorize("hasAuthority(T(com.proveritus.cloudutility.security.Permissions.Floor).CREATE) and @apiFeatureControl.isEnabled('floors', 'bulk-create')")
    @Operation(summary = "Create multiple floors")
    @Auditable
    public ResponseEntity<Void> createFloors(@Valid @RequestBody List<FloorDTO> floorDTOs) {
        log.info("Creating {} floors", floorDTOs.size());
        floorService.createFloors(floorDTOs);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/bulk-update")
    @PreAuthorize("hasAuthority(T(com.proveritus.cloudutility.security.Permissions.Floor).UPDATE) and @apiFeatureControl.isEnabled('floors', 'bulk-update')")
    @Operation(summary = "Update multiple floors")
    @Auditable
    public ResponseEntity<Void> updateFloors(@Valid @RequestBody List<FloorDTO> floorDTOs) {
        log.info("Updating {} floors", floorDTOs.size());
        floorService.updateFloors(floorDTOs);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/bulk-delete")
    @PreAuthorize("hasAuthority(T(com.proveritus.cloudutility.security.Permissions.Floor).DELETE) and @apiFeatureControl.isEnabled('floors', 'bulk-delete')")
    @Operation(summary = "Delete multiple floors")
    @Auditable
    public ResponseEntity<Void> deleteFloors(@RequestBody List<Long> ids) {
        log.info("Deleting {} floors", ids.size());
        floorService.deleteFloors(ids);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/occupancy-stats")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get occupancy statistics for a floor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Statistics retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Floor not found")
    })
    @Auditable
    public ResponseEntity<FloorOccupancyStats> getFloorOccupancyStats(
            @Parameter(description = "ID of the floor") @PathVariable Long id) {
        log.debug("Fetching occupancy stats for floor ID: {}", id);
        return ResponseEntity.ok(floorService.getFloorOccupancyStats(id));
    }

    @PostMapping("/{id}/refresh-occupancy")
    @PreAuthorize("hasAuthority(T(com.proveritus.cloudutility.security.Permissions.Floor).UPDATE) and @apiFeatureControl.isEnabled('floors', 'refresh-occupancy')")
    @Operation(summary = "Refresh occupancy statistics for a floor")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Occupancy stats refreshed successfully"),
            @ApiResponse(responseCode = "404", description = "Floor not found")
    })
    @Auditable
    public ResponseEntity<Void> refreshFloorOccupancy(
            @Parameter(description = "ID of the floor") @PathVariable Long id) {
        log.info("Refreshing occupancy stats for floor ID: {}", id);
        floorService.updateFloorOccupancyStats(id);
        return ResponseEntity.ok().build();
    }
}
