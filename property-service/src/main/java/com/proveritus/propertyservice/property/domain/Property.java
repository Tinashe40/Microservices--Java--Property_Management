package com.proveritus.propertyservice.property.domain;

import com.proveritus.cloudutility.enums.PropertyType;
import com.proveritus.cloudutility.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "properties")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class Property extends BaseEntity {
    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PropertyType propertyType;

    @Column(nullable = false)
    private String address;

    private Integer numberOfFloors;
    private Integer numberOfUnits;

    private Long managedBy;
}
