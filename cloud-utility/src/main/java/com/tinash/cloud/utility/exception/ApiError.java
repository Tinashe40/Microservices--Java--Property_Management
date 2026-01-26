package com.tinash.cloud.utility.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Standard API error response structure.
 * Used for returning detailed error information to clients.
 */
@Data
public class ApiError {

    private HttpStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    private String message;
    private String debugMessage;
    private List<ApiSubError> subErrors;

    private ApiError() {
        timestamp = LocalDateTime.now();
    }

    public ApiError(HttpStatus status) {
        this();
        this.status = status;
    }

    public ApiError(HttpStatus status, Throwable ex) {
        this();
        this.status = status;
        this.message = "Unexpected error";
        this.debugMessage = ex.getLocalizedMessage();
    }

    public ApiError(HttpStatus status, String message, Throwable ex) {
        this();
        this.status = status;
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }

    // Make this method public so it can be used from GlobalExceptionHandler
    public void addSubError(ApiSubError subError) {
        if (subErrors == null) {
            subErrors = new ArrayList<>();
        }
        subErrors.add(subError);
    }

    // Helper methods for validation errors
    public void addValidationErrors(List<FieldError> fieldErrors) {
        fieldErrors.forEach(fieldError ->
                addSubError(new ApiValidationError(
                        fieldError.getObjectName(),
                        fieldError.getField(),
                        fieldError.getRejectedValue(),
                        fieldError.getDefaultMessage()
                ))
        );
    }

    public void addValidationError(List<ObjectError> globalErrors) {
        globalErrors.forEach(globalError ->
                addSubError(new ApiValidationError(
                        globalError.getObjectName(),
                        globalError.getDefaultMessage()
                ))
        );
    }

    /**
     * Base interface for all sub-errors.
     */
    public interface ApiSubError {
    }

    /**
     * Sub-error for validation errors.
     */
    @Data
    public static class ApiValidationError implements ApiSubError {
        private String object;
        private String field;
        private Object rejectedValue;
        private String message;

        public ApiValidationError(String object, String message) {
            this.object = object;
            this.message = message;
        }

        public ApiValidationError(String object, String field, Object rejectedValue, String message) {
            this.object = object;
            this.field = field;
            this.rejectedValue = rejectedValue;
            this.message = message;
        }
    }
}