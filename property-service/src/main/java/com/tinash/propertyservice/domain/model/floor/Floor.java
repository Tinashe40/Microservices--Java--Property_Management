package com.tinash.propertyservice.domain.model.floor;

import com.tinash.cloud.utility.jpa.BaseEntity;
import com.tinash.propertyservice.domain.model.property.Property;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "floors")
@Getter
@Setter
public class Floor extends BaseEntity {
    @Id
    @GeneratedValue(generator = "ulid-generator")
    @GenericGenerator(
            name = "ulid-generator",
            type = HibernateIdGeneratorAdapter.class,
            parameters = @org.hibernate.annotations.Parameter(name = "prefix", value = IdPrefix.FLOOR)
    )
    private String id;

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
