package com.proveritus.cloudutility.audit.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.proveritus.cloudutility.audit.annotation.Auditable;
import com.proveritus.cloudutility.audit.entity.AuditLog;
import com.proveritus.cloudutility.audit.repository.AuditLogRepository;
import com.proveritus.cloudutility.jpa.BaseEntity;
import com.proveritus.cloudutility.security.CustomPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component("auditAspect")
@RequiredArgsConstructor
public class AuditAspect {

    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;

    @Around("@annotation(auditable)")
    public Object audit(ProceedingJoinPoint joinPoint, Auditable auditable) throws Throwable {
        Object result = null;
        String errorMessage = null;
        String outcome = "SUCCESS";

        try {
            result = joinPoint.proceed();
            return result;
        } catch (Throwable e) {
            outcome = "FAILURE";
            errorMessage = e.getMessage();
            throw e;
        } finally {
            try {
                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                Long userId = null;
                String userName = null;

                if (principal instanceof CustomPrincipal customPrincipal) {
                    userId = customPrincipal.getId();
                    userName = customPrincipal.getUsername();
                } else {
                    userName = principal.toString();
                }

                Long entityId = null;
                if (result instanceof BaseEntity) {
                    entityId = ((BaseEntity) result).getId();
                }

                AuditLog auditLog = AuditLog.builder()
                        .methodName(joinPoint.getSignature().toShortString())
                        .params(objectMapper.writeValueAsString(joinPoint.getArgs()))
                        .userId(userId)
                        .userName(userName)
                        .timestamp(LocalDateTime.now())
                        .action(auditable.action())
                        .entity(auditable.entity())
                        .entityId(entityId)
                        .outcome(outcome)
                        .errorMessage(errorMessage)
                        .build();

                auditLogRepository.save(auditLog);
            } catch (Exception e) {
                log.error("Failed to persist audit log for method: {}",
                        joinPoint.getSignature().toShortString(), e);
            }
        }
    }
}
