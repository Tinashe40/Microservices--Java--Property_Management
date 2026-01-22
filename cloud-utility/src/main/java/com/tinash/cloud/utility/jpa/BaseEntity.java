package com.tinash.cloud.utility.jpa;

import com.tinash.cloud.utility.audit.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Base entity class with common fields for all entities.
 * Provides ID and auditing fields.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @Version
    private Long version;
}
