package com.proveritus.propertyservice.domain.model.property;

import com.tinash.cloud.utility.core.domain.BaseEntity;
import com.tinash.cloud.utility.core.domain.DomainEvent;
import com.proveritus.propertyservice.domain.model.floor.Floor;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Property aggregate root with rich domain model.
 * Contains business logic and invariants.
 */
@Entity
@Table(name = "properties", indexes = {
        @Index(name = "idx_property_name", columnList = "name"),
        @Index(name = "idx_property_manager", columnList = "managed_by")
})
@Getter
public class Property extends BaseEntity<String> {

    @Id
    private String id;

    @Embedded
    private PropertyName name;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    @Column(name = "property_type", nullable = false)
    private PropertyType propertyType;

    @Column(name = "managed_by")
    private String managedBy;

    @Column(name = "number_of_floors")
    private Integer numberOfFloors;

    @Column(name = "number_of_units")
    private Integer numberOfUnits;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Floor> floors = new ArrayList<>();

    @Transient
    private List<DomainEvent> domainEvents = new ArrayList<>();

    protected Property() {
        // JPA constructor
    }

    /**
     * Factory method to create a new property.
     * Enforces business rules at creation time.
     */
    public static Property create(
            PropertyId id,
            PropertyName name,
            Address address,
            PropertyType propertyType,
            String managerId) {

        Property property = new Property();
        property.id = id.value();
        property.name = name;
        property.address = address;
        property.propertyType = propertyType;
        property.managedBy = managerId;
        property.numberOfFloors = 0;
        property.numberOfUnits = 0;

        property.registerEvent(new PropertyCreatedEvent(
                property.id, 
                name.value(), 
                managerId
        ));

        return property;
    }

    /**
     * Adds a floor to the property.
     * Maintains aggregate consistency.
     */
    public void addFloor(Floor floor) {
        if (this.floors.contains(floor)) {
            throw new IllegalStateException("Floor already exists in this property");
        }

        this.floors.add(floor);
        this.numberOfFloors = this.floors.size();

        registerEvent(new FloorAddedEvent(this.id, floor.getId()));
    }

    /**
     * Removes a floor from the property.
     * Validates business rules before removal.
     */
    public void removeFloor(Floor floor) {
        if (!floor.isEmpty()) {
            throw new IllegalStateException("Cannot remove floor with existing units");
        }

        this.floors.remove(floor);
        this.numberOfFloors = this.floors.size();

        registerEvent(new FloorRemovedEvent(this.id, floor.getId()));
    }

    /**
     * Updates the property details.
     */
    public void updateDetails(PropertyName name, Address address, PropertyType type) {
        this.name = name;
        this.address = address;
        this.propertyType = type;

        registerEvent(new PropertyUpdatedEvent(this.id));
    }

    /**
     * Assigns a new manager to the property.
     */
    public void assignManager(String managerId) {
        String previousManager = this.managedBy;
        this.managedBy = managerId;

        registerEvent(new PropertyManagerChangedEvent(
                this.id, 
                previousManager, 
                managerId
        ));
    }

    /**
     * Recalculates aggregate statistics.
     */
    public void recalculateStatistics() {
        this.numberOfFloors = this.floors.size();
        this.numberOfUnits = this.floors.stream()
                .mapToInt(Floor::getUnitCount)
                .sum();
    }

    /**
     * Checks if property can be deleted.
     */
    public boolean canBeDeleted() {
        return this.floors.isEmpty();
    }

    // Domain event management
    protected void registerEvent(DomainEvent event) {
        this.domainEvents.add(event);
    }

    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    public void clearEvents() {
        this.domainEvents.clear();
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }
}
