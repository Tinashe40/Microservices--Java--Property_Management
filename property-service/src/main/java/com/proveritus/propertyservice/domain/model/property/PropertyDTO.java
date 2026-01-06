package com.proveritus.propertyservice.domain.model.property;

import com.proveritus.cloudutility.dto.UserDTO;

public record PropertyDTO(String id, String name, String managedBy) {
    public PropertyDTO withManagerDetails(UserDTO user) {
        return new PropertyDTO(this.id, this.name, user.getUsername());
    }
}
