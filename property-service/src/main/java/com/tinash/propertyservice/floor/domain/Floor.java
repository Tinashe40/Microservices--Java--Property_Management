package com.tinash.propertyservice.floor.domain;

import com.tinash.cloud.utility.jpa.BaseEntity;
import com.tinash.propertyservice.property.domain.Property;
import com.tinash.propertyservice.unit.domain.Unit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "floors")
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
