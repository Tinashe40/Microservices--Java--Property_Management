package com.proveritus.cloudutility.audit.listener;

import com.proveritus.cloudutility.audit.model.AuditLog;
import com.proveritus.cloudutility.audit.service.AuditService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AuditEventListener {

    private final AuditService auditService;

    public AuditEventListener(AuditService auditService) {
        this.auditService = auditService;
    }

    @EventListener
    public void onAuditEvent(AuditLog auditLog) {
        auditService.log(auditLog);
    }
}