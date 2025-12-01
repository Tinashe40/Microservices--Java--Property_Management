package com.proveritus.propertyservice.property.domain;

import com.proveritus.cloudutility.enums.PropertyType;
import com.proveritus.cloudutility.jpa.BaseEntity;
import com.proveritus.propertyservice.unit.domain.Unit;
import com.proveritus.propertyservice.floor.domain.Floor;
import jakarta.persistence.*;
import lombok.*;
// import org.springframework.data.elasticsearch.annotations.Document;
// import org.springframework.data.elasticsearch.annotations.Field;
// import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "properties")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// @Document(indexName = "properties")
public class Property extends BaseEntity {
    @Column(nullable = false)
    // @Field(type = FieldType.Text, name = "name")
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    // @Field(type = FieldType.Keyword, name = "propertyType")
    private PropertyType propertyType;

    @Column(nullable = false)
    // @Field(type = FieldType.Text, name = "address")
    private String address;

    private Integer numberOfFloors;
    private Integer numberOfUnits;

    private Long managedBy;
}
