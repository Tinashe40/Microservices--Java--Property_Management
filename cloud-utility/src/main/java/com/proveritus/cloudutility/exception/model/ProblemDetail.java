package com.proveritus.cloudutility.exception.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.Map;

/**
 * API error detail following RFC 7807 (Problem Details for HTTP APIs).
 */
 @Getter
 @JsonInclude(JsonInclude.Include.NON_NULL)
public class ProblemDetail {
    
    private final String type;
    private final String title;
    private final int status;
    private final String detail;
    private final String instance;
    private final Map<String, Object> additionalProperties;

    public ProblemDetail(String type, String title, int status, String detail, String instance, Map<String, Object> additionalProperties) {
        this.type = type;
        this.title = title;
        this.status = status;
        this.detail = detail;
        this.instance = instance;
        this.additionalProperties = additionalProperties;
    }
}