package com.proveritus.propertyservice.floor.domain;

import com.proveritus.cloudutility.jpa.BaseEntity;
import com.proveritus.propertyservice.property.domain.Property;
import com.proveritus.propertyservice.unit.domain.Unit;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "floors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Floor extends BaseEntity {
    @Column(nullable = false)
    private String name;

    private Integer numberOfUnits;
    private Integer occupiedUnits;
    private Integer vacantUnits;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @Builder.Default
    @OneToMany(mappedBy = "floor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Unit> units = new ArrayList<>();
}
