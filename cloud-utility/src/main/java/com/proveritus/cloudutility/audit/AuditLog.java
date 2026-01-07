package com.proveritus.cloudutility.audit;

import com.proveritus.cloudutility.core.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Getter
@Setter
public class AuditLog extends BaseEntity<Long> {
    @Id
    private Long id;
    private String action;
    private String entity;
    private Long entityId;

}
