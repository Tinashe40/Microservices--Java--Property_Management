package com.proveritus.cloudutility.audit.service;

import com.proveritus.cloudutility.audit.model.AuditLog;

public interface AuditService {

    void log(AuditLog auditLog);
}