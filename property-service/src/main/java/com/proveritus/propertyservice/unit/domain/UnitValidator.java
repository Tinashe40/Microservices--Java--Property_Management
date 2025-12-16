package com.proveritus.propertyservice.unit.domain;

import com.proveritus.cloudutility.enums.RentType;
import com.proveritus.cloudutility.exception.*;
import com.proveritus.propertyservice.floor.domain.FloorRepository;
import com.proveritus.propertyservice.property.domain.PropertyRepository;
import com.proveritus.propertyservice.unit.dto.UnitDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UnitValidator {

    private final UnitRepository unitRepository;
    private final PropertyRepository propertyRepository;
    private final FloorRepository floorRepository;

    public void validate(UnitDTO unitDTO) {
        validate(unitDTO, null);
    }

    public void validate(UnitDTO unitDTO, Long excludedUnitId) {
        unitRepository.findByNameAndPropertyId(unitDTO.getName(), unitDTO.getPropertyId())
                .ifPresent(existingUnit -> {
                    if (excludedUnitId == null || !existingUnit.getId().equals(excludedUnitId)) {
                        throw new IllegalArgumentException("Unit with name " + unitDTO.getName() +
                                " already exists in property with id: " + unitDTO.getPropertyId());
                    }
                });

        if (unitDTO.getName() == null || unitDTO.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Unit name cannot be empty");
        }
        if (unitDTO.getSize() != null && unitDTO.getSize() <= 0) {
            throw new IllegalArgumentException("Unit size must be greater than 0");
        }
        if (unitDTO.getMonthlyRent() != null && unitDTO.getMonthlyRent() < 0) {
            throw new IllegalArgumentException("Monthly rent cannot be negative");
        }
        if (unitDTO.getRentType() == RentType.PSM &&
                unitDTO.getRatePerSqm() != null &&
                unitDTO.getRatePerSqm() < 0) {
            throw new IllegalArgumentException("Rate per square meter cannot be negative");
        }

        if (!propertyRepository.existsById(unitDTO.getPropertyId())) {
            throw new ResourceNotFoundException("Property not found with id: " + unitDTO.getPropertyId());
        }

        if (unitDTO.getFloorId() != null && !floorRepository.existsById(unitDTO.getFloorId())) {
            throw new ResourceNotFoundException("Floor not found with id: " + unitDTO.getFloorId());
        }
    }
}
