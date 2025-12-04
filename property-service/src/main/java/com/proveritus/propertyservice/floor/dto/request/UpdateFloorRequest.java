package com.proveritus.propertyservice.floor.dto.request;

import com.proveritus.cloudutility.jpa.Updatable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateFloorRequest implements Updatable {

    private Long id;

    @NotNull(message = "Property ID is required")
    private Long propertyId;

    @NotBlank(message = "Floor name is required")
    private String name;

    @Override
    public Long getId() {
        return id;
    }
}
