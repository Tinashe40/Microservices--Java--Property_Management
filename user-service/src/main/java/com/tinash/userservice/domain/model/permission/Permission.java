package com.tinash.userservice.domain.model.permission;

import com.tinash.userservice.domain.model.IdPrefix;
import com.tinash.userservice.shared.domain.id.HibernateIdGeneratorAdapter;
import com.tinash.cloud.utility.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "permissions")
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class Permission extends BaseEntity {
    @Id
    @GeneratedValue(generator = "ulid-generator")
    @GenericGenerator(
            name = "ulid-generator",
            type = HibernateIdGeneratorAdapter.class,
            parameters = @org.hibernate.annotations.Parameter(name = "prefix", value = IdPrefix.PERMISSION)
    )
    @Column(unique = true, nullable = false)
    private String name;
}