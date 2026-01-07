package com.proveritus.cloudutility.dto.base;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public abstract class BaseDTO<ID extends Serializable> {

    private ID id;
}