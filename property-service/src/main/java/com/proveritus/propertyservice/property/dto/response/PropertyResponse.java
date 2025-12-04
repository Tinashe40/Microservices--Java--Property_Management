package com.proveritus.propertyservice.property.dto.response;

import com.proveritus.propertyservice.floor.dto.response.FloorResponse;
import com.proveritus.cloudutility.dto.UserDTO;
import com.proveritus.cloudutility.enums.PropertyType;
import com.proveritus.propertyservice.unit.dto.response.UnitResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertyResponse {

    private Long id;
    private String name;
    private PropertyType propertyType;
    private String address;
    private Integer numberOfFloors;
    private Integer numberOfUnits;
    private Long managedBy;
    private UserDTO managedByDetails;
    private List<FloorResponse> floors;
    private List<UnitResponse> units;
}
