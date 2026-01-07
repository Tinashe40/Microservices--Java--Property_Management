package com.proveritus.cloudutility.jpa.listener;

import com.proveritus.cloudutility.jpa.entity.AuditableEntity;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class AuditingEntityListener {

    @PrePersist
    public void onPrePersist(AuditableEntity<?> auditable) {
        auditable.setCreatedDate(Instant.now());
        auditable.setLastModifiedDate(Instant.now());
        getPrincipal().ifPresent(p -> {
            auditable.setCreatedBy(p);
            auditable.setLastModifiedBy(p);
        });
    }

    @PreUpdate
    public void onPreUpdate(AuditableEntity<?> auditable) {
        auditable.setLastModifiedDate(Instant.now());
        getPrincipal().ifPresent(auditable::setLastModifiedBy);
    }

    private java.util.Optional<String> getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return java.util.Optional.empty();
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return java.util.Optional.of(((UserDetails) principal).getUsername());
        }
        if (principal instanceof String) {
            return java.util.Optional.of((String) principal);
        }
        return java.util.Optional.empty();
    }
}