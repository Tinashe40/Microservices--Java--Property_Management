package com.proveritus.cloudutility.dto.base;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * Base DTO with common fields.
 */
@Data
public abstract class BaseDto extends Serializable{
    private ID id;
    private String createdBy;
    private LocalDateTime createdAt;
    private String modifiedBy;
    private LocalDateTime modifiedAt;
}
