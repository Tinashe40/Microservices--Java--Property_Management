package com.proveritus.propertyservice.unit.domain;

import com.proveritus.cloudutility.enums.*;
import com.proveritus.cloudutility.jpa.BaseEntity;
import com.proveritus.propertyservice.floor.domain.Floor;
import com.proveritus.propertyservice.property.domain.Property;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Entity
@Table(name = "units")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Unit extends BaseEntity {

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Positive
    private Double size;

    @Enumerated(EnumType.STRING)
    private RentType rentType;

    @Positive
    private Double ratePerSqm;

    @Positive
    private Double monthlyRent;

    @Enumerated(EnumType.STRING)
    private OccupancyStatus occupancyStatus;

    private String tenant;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id")
    private Floor floor;
}
