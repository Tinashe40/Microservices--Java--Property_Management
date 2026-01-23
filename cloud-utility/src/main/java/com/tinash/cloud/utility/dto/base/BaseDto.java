package com.tinash.cloud.utility.dto.base;

import lombok.Data;

import java.io.Serializable;

@Data
public abstract class BaseDto<ID extends Serializable> {
    private ID id;
}
