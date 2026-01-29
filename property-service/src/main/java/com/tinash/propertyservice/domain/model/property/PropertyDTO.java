package com.tinash.propertyservice.domain.model.property;

import com.tinash.cloud.utility.dto.UserDto;

public record PropertyDTO(String id, String name, String managedBy) {
    public PropertyDTO withManagerDetails(UserDto user) {
        return new PropertyDTO(this.id, this.name, user.getUsername());
    }
}
