package com.proveritus.propertyservice.domain.model.floor;

import com.tinash.cloud.utility.jpa.entity;
import com.proveritus.propertyservice.domain.model.property.Property;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "floors")
@Getter
@Setter
public class Floor extends BaseEntity extends SoftDeletableEntity extends AuditableEntity {


    @ManyToOne
    private Property property;

    public boolean isEmpty() {
        // TODO: Implement logic to check if floor has units
        return true;
    }

    public int getUnitCount() {
        // TODO: Implement logic to get unit count
        return 0;
    }
}
