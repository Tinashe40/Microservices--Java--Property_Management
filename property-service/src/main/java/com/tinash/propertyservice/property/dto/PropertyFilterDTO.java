package com.tinash.propertyservice.property.dto;

import com.tinash.cloud.utility.enums.PropertyType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PropertyFilterDTO {
    private String name;
    private String address;
    private PropertyType propertyType;
    private Integer minFloors;
    private Integer maxFloors;
    private Integer minUnits;
    private Integer maxUnits;
}
