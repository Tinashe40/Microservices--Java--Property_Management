package com.proveritus.cloudutility.audit.model;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class AuditLog {

    private String id;
    private String user;
    private String action;
    private Instant timestamp;
    private String details;
}