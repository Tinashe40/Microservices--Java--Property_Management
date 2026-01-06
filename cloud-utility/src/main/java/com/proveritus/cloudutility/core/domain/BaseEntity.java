package com.proveritus.cloudutility.core.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * Generic base entity providing audit fields and soft delete capability.
 * Follows SOLID principles with proper generic type parameter.
 *
 * @param <ID> the type of the entity identifier
 * @author Enterprise Team
 * @since 1.0
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity<ID extends Serializable> implements Serializable {

    @CreatedBy @Column(name = "created_by", nullable = false, updatable = false, length = 100)
    private String createdBy;

    @CreatedDate @Column(name = "created_date", nullable = false, updatable = false)
    private Instant createdDate;

    @LastModifiedBy @Column(name = "last_modified_by", length = 100)
    private String lastModifiedBy;

    @LastModifiedDate @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @Version @Column(name = "version")
    private Long version;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    /**
     * Gets the entity identifier.
     *
     * @return the entity ID
     */
    public abstract ID getId();

    /**
     * Sets the entity identifier.
     *
     * @param id the entity ID to set
     */
    public abstract void setId(ID id);

    /**
     * Performs soft delete on the entity.
     * This marks the entity as deleted without removing it from the database.
     */
    public void delete() {
        this.deleted = true;
    }

    /**
     * Restores a soft-deleted entity.
     */
    public void restore() {
        this.deleted = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity<?> that = (BaseEntity<?>) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass().getName());
    }
}
