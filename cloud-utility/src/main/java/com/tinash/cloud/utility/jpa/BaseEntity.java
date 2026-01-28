package com.tinash.cloud.utility.jpa;

import com.tinash.cloud.utility.audit.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * Base entity class with common fields for all entities.
 * Provides ID and auditing fields.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity extends Auditable {

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @Version
    private Long version;
}
