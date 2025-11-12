package com.proveritus.propertyservice.property.dto;

import com.proveritus.propertyservice.floor.dto.FloorDTO;
import com.proveritus.cloudutility.dto.UserDTO;
import com.proveritus.cloudutility.enums.PropertyType;
import com.proveritus.propertyservice.unit.dto.UnitDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyDTO {

    private String name;
    private PropertyType propertyType;
    private String address;
    private Integer numberOfFloors;
    private Integer numberOfUnits;
    private Long managedBy;
    private UserDTO managedByDetails;
    private List<FloorDTO> floors;
    private List<UnitDTO> units;
}
