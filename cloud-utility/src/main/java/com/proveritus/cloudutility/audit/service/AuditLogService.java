package com.proveritus.cloudutility.audit.service;

import com.proveritus.cloudutility.audit.model.AuditLog;
import com.proveritus.cloudutility.audit.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

@Service
public class AuditLogService implements AuditService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    public void log(AuditLog auditLog) {
        auditLogRepository.save(auditLog);
    }
}