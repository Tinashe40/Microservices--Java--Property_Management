package com.proveritus.propertyservice.floor.domain;

import com.proveritus.propertyservice.floor.dto.FloorDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FloorValidator {

    private final FloorRepository floorRepository;

    public void validate(FloorDTO floorDTO) {
        if (floorRepository.findByNameAndPropertyId(floorDTO.getName(), floorDTO.getPropertyId()).isPresent()) {
            throw new IllegalArgumentException("Floor with name " + floorDTO.getName() + " already exists in property with id: " + floorDTO.getPropertyId());
        }
        if (floorDTO.getName() == null || floorDTO.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Floor name cannot be empty");
        }
    }

    public void checkForDuplicateFloorName(String name, Long propertyId) {
        if (floorRepository.findByNameAndPropertyId(name, propertyId).isPresent()) {
            throw new IllegalArgumentException(
                    String.format("Floor with name '%s' already exists in property with id: %d", name, propertyId)
            );
        }
    }
}
