package com.proveritus.cloudutility.core.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class AggregateRoot<ID extends Serializable> extends BaseEntity<ID> {

    private final transient List<DomainEvent> domainEvents = new ArrayList<>();

    protected void registerEvent(DomainEvent event) {
        domainEvents.add(event);
    }

    public List<DomainEvent> getDomainEvents() {
        return domainEvents;
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }
}