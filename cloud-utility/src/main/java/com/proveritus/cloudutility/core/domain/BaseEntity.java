package com.proveritus.cloudutility.core.domain;

import java.io.Serializable;

public abstract class BaseEntity<ID extends Serializable> {

    private ID id;

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }
}
