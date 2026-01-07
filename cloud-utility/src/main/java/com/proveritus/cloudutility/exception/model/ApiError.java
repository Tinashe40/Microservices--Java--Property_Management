package com.proveritus.cloudutility.exception.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {

    private final String message;
    private final String debugMessage;
    private final List<ValidationError> validationErrors;

    public ApiError(String message, String debugMessage, List<ValidationError> validationErrors) {
        this.message = message;
        this.debugMessage = debugMessage;
        this.validationErrors = validationErrors;
    }
}
