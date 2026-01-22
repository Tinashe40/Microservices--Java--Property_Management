package com.proveritus.cloudutility.exception.handler;

import com.tinash.cloud.utility.exception.model.ApiError;
import com.tinash.cloud.utility.exception.model.ValidationError;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<ValidationError> validationErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ValidationError(error.getField(), error.getRejectedValue() != null ? error.getRejectedValue().toString() : "null", error.getDefaultMessage()))
                .collect(Collectors.toList());
        ApiError apiError = new ApiError("Validation failed", ex.getLocalizedMessage(), validationErrors);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }
}
