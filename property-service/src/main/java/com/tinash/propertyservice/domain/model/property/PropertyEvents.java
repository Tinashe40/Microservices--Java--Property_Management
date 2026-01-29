package com.tinash.propertyservice.domain.model.property;

import com.tinash.cloud.utility.core.domain.DomainEvent;

public class PropertyUpdatedEvent extends DomainEvent {
    private final String propertyId;

    public PropertyUpdatedEvent(String propertyId) {
        super();
        this.propertyId = propertyId;
    }

    public String getPropertyId() {
        return propertyId;
    }
}

class PropertyManagerChangedEvent extends DomainEvent {
    private final String propertyId;
    private final String previousManagerId;
    private final String newManagerId;

    public PropertyManagerChangedEvent(String propertyId, String previousManagerId, String newManagerId) {
        super();
        this.propertyId = propertyId;
        this.previousManagerId = previousManagerId;
        this.newManagerId = newManagerId;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public String getPreviousManagerId() {
        return previousManagerId;
    }

    public String getNewManagerId() {
        return newManagerId;
    }
}

class FloorAddedEvent extends DomainEvent {
    private final String propertyId;
    private final String floorId;

    public FloorAddedEvent(String propertyId, String floorId) {
        this.propertyId = propertyId;
        this.floorId = floorId;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public String getFloorId() {
        return floorId;
    }
}

class FloorRemovedEvent extends DomainEvent {
    private final String propertyId;
    private final String floorId;

    public FloorRemovedEvent(String propertyId, String floorId) {
        this.propertyId = propertyId;
        this.floorId = floorId;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public String getFloorId() {
        return floorId;
    }
}
