package com.proveritus.propertyservice.property.dto.request;

import com.proveritus.cloudutility.enums.PropertyType;
import com.proveritus.cloudutility.jpa.Updatable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePropertyRequest implements Updatable {

    private Long id;

    @NotBlank(message = "Property name is required")
    private String name;

    @NotNull(message = "Property type is required")
    private PropertyType propertyType;

    @NotBlank(message = "Property address is required")
    private String address;

    private Integer numberOfFloors;
    private Integer numberOfUnits;

    @Override
    public Long getId() {
        return id;
    }
}
