package com.proveritus.cloudutility.dto.base;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
public abstract class AuditableDTO<ID extends Serializable> extends BaseDTO<ID> {

    private String createdBy;
    private Instant createdDate;
    private String lastModifiedBy;
    private Instant lastModifiedDate;
}