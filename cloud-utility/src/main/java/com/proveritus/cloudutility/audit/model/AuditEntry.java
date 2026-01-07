package com.proveritus.cloudutility.audit.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditEntry {

    private String field;
    private String oldValue;
    private String newValue;
}