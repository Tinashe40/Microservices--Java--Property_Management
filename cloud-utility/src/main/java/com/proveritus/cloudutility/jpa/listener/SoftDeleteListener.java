package com.proveritus.cloudutility.jpa.listener;

import com.proveritus.cloudutility.jpa.entity.SoftDeletableEntity;
import jakarta.persistence.PreRemove;
import org.springframework.stereotype.Component;

@Component
public class SoftDeleteListener {

    @PreRemove
    public void onPreRemove(SoftDeletableEntity<?> entity) {
        entity.setDeleted(true);
        throw new IllegalStateException("Soft delete is not yet implemented. You should not be able to delete this entity.");
    }
}