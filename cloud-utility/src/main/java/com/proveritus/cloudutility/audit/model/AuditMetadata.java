package com.proveritus.cloudutility.audit.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuditMetadata {

    private String correlationId;
    private String ipAddress;
}