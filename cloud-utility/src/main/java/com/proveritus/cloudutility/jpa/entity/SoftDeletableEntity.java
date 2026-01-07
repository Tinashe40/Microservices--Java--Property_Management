package com.proveritus.cloudutility.jpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@MappedSuperclass
@Getter
@Setter
public abstract class SoftDeletableEntity<ID extends Serializable> extends AuditableEntity<ID> {

    @Column(name = "is_deleted")
    private boolean deleted = false;
}