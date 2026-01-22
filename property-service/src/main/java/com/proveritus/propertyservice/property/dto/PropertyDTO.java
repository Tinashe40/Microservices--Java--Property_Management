package com.proveritus.propertyservice.property.dto;

import com.proveritus.propertyservice.floor.dto.FloorDTO;
import com.tinash.cloud.utility.dto.UserDto;
import com.tinash.cloud.utility.enums.PropertyType;
import com.tinash.cloud.utility.jpa.Updatable;
import com.proveritus.propertyservice.unit.dto.UnitDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyDTO implements Updatable {

    private String id;
    private String name;
    private PropertyType propertyType;
    private String address;
    private Integer numberOfFloors;
    private Integer numberOfUnits;
    private String managedBy;
    private UserDto managedByDetails;
    private List<FloorDTO> floors;
    private List<UnitDTO> units;
}
