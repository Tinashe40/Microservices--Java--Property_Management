package com.proveritus.propertyservice.unit.domain;

import com.tinash.cloud.utility.jpa.BaseEntity;
import com.proveritus.propertyservice.floor.domain.Floor;
import com.proveritus.propertyservice.property.domain.Property;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "units")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Unit extends BaseEntity {

    @NotBlank
    @Column(nullable = false)
    private String name;

    @Positive
    private Double size;

    @Enumerated(EnumType.STRING)
    private com.proveritus.cloudutility.enums.RentType rentType;

    @Positive
    private Double ratePerSqm;

    @Positive
    private Double monthlyRent;

    @Enumerated(EnumType.STRING)
    private com.proveritus.cloudutility.enums.OccupancyStatus occupancyStatus;

    private String tenant;

    private String tenantEmail;

    private String tenantPhone;

    private LocalDate leaseStartDate;

    private LocalDate leaseEndDate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "floor_id")
    private Floor floor;
}
