package com.proveritus.cloudutility.audit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String methodName;

    private String params;

    private Long userId;

    private String userName;

    private LocalDateTime timestamp;

    private String action;

    private String entity;

    private Long entityId;

    private String outcome;

    private String errorMessage;
}
