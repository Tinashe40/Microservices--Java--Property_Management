package com.proveritus.propertyservice.domain.model.property;

import com.tinash.cloud.utility.core.domain.DomainEvent;

public class PropertyCreatedEvent extends DomainEvent {
    private final String propertyId;
    private final String propertyName;
    private final String managerId;

    public PropertyCreatedEvent(String propertyId, String propertyName, String managerId) {
        super();
        this.propertyId = propertyId;
        this.propertyName = propertyName;
        this.managerId = managerId;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getManagerId() {
        return managerId;
    }
}
