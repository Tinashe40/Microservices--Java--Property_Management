package com.tinash.propertyservice.floor.dto;

import com.tinash.cloud.utility.jpa.Updatable;
import com.tinash.propertyservice.unit.dto.UnitDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FloorDTO implements Updatable {

    private String id;

    @NotNull(message = "Property ID is required")
    private String propertyId;

    @NotBlank(message = "Floor name is required")
    private String name;
    private String propertyType;
    private String address;

    private Integer numberOfUnits;
    private Integer occupiedUnits;
    private Integer vacantUnits;
    private List<UnitDTO> units;
}
