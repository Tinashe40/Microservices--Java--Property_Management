package com.proveritus.cloudutility.exception.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Standardized error response for API consumers.
 */
 @Getter
 @Builder
 @JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    
    private final String errorId;
    private final String errorCode;
    private final String message;
    private final int status;
    private final Instant timestamp;
    private final String path;
    private final Map<String, List<String>> fieldErrors;
    private final String debugMessage;
    private final List<String> stackTrace;
}