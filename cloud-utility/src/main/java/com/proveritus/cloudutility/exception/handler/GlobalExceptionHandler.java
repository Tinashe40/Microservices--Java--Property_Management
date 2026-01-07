package com.proveritus.cloudutility.exception.handler;

import com.proveritus.cloudutility.exception.base.BusinessException;
import com.proveritus.cloudutility.exception.base.TechnicalException;
import com.proveritus.cloudutility.exception.business.ResourceNotFoundException;
import com.proveritus.cloudutility.exception.business.ValidationException;
import com.proveritus.cloudutility.exception.model.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.Instant;
import java.util.*;

/**
 * Global exception handler for REST controllers.
 * Provides consistent error responses across the application.
 */
@Slf4j
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    /**
     * Handle custom business exceptions.
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            BusinessException ex, 
            HttpServletRequest request) {
        
        log.warn("Business exception: {}", ex.getMessage());
        
        ErrorResponse response = ErrorResponse.builder()
                .errorId(ex.getErrorId())
                .errorCode(ex.getErrorCode())
                .message(ex.getMessage())
                .status(ex.getHttpStatus())
                .timestamp(ex.getTimestamp())
                .path(request.getRequestURI())
                .build();
        
        return ResponseEntity.status(ex.getHttpStatus()).body(response);
    }

    /**
     * Handle custom technical exceptions.
     */
    @ExceptionHandler(TechnicalException.class)
    public ResponseEntity<ErrorResponse> handleTechnicalException(
            TechnicalException ex, 
            HttpServletRequest request) {
        
        log.error("Technical exception: {}", ex.getMessage(), ex);
        
        ErrorResponse response = ErrorResponse.builder()
                .errorId(ex.getErrorId())
                .errorCode(ex.getErrorCode())
                .message("An internal error occurred. Please contact support with error ID: " + ex.getErrorId())
                .status(ex.getHttpStatus())
                .timestamp(ex.getTimestamp())
                .path(request.getRequestURI())
                .debugMessage(ex.getMessage()) // Only in non-prod
                .build();
        
        return ResponseEntity.status(ex.getHttpStatus()).body(response);
    }

    /**
     * Handle validation exceptions.
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            ValidationException ex, 
            HttpServletRequest request) {
        
        log.warn("Validation exception: {}", ex.getMessage());
        
        ErrorResponse response = ErrorResponse.builder()
                .errorId(ex.getErrorId())
                .errorCode(ex.getErrorCode())
                .message(ex.getMessage())
                .status(ex.getHttpStatus())
                .timestamp(ex.getTimestamp())
                .path(request.getRequestURI())
                .fieldErrors(ex.getFieldErrors())
                .build();
        
        return ResponseEntity.status(ex.getHttpStatus()).body(response);
    }

    /**
     * Handle Spring's method argument validation failures.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, 
            HttpServletRequest request) {
        
        log.warn("Method argument validation failed: {}", ex.getMessage());
        
        Map<String, List<String>> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String field = error.getField();
            String message = error.getDefaultMessage();
            fieldErrors.computeIfAbsent(field, k -> new ArrayList<>()).add(message);
        });
        
        ErrorResponse response = ErrorResponse.builder()
                .errorId(UUID.randomUUID().toString())
                .errorCode("VALIDATION_ERROR")
                .message("Validation failed for one or more fields")
                .status(HttpStatus.BAD_REQUEST.value())
                .timestamp(Instant.now())
                .path(request.getRequestURI())
                .fieldErrors(fieldErrors)
                .build();
        
        return ResponseEntity.badRequest().body(response);
    }

    /**
     * Handle resource not found exceptions.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(
            ResourceNotFoundException ex, 
            HttpServletRequest request) {
        
        log.warn("Resource not found: {}", ex.getMessage());
        
        ErrorResponse response = ErrorResponse.builder()
                .errorId(ex.getErrorId())
                .errorCode(ex.getErrorCode())
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(ex.getTimestamp())
                .path(request.getRequestURI())
                .build();
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Handle authentication exceptions.
     */
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(
            AuthenticationException ex, 
            HttpServletRequest request) {
        
        log.warn("Authentication failed: {}", ex.getMessage());
        
        ErrorResponse response = ErrorResponse.builder()
                .errorId(UUID.randomUUID().toString())
                .errorCode("AUTHENTICATION_FAILED")
                .message("Authentication failed: " + ex.getMessage())
                .status(HttpStatus.UNAUTHORIZED.value())
                .timestamp(Instant.now())
                .path(request.getRequestURI())
                .build();
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    /**
     * Handle access denied exceptions.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(
            AccessDeniedException ex, 
            HttpServletRequest request) {
        
        log.warn("Access denied: {}", ex.getMessage());
        
        ErrorResponse response = ErrorResponse.builder()
                .errorId(UUID.randomUUID().toString())
                .errorCode("ACCESS_DENIED")
                .message("You don't have permission to access this resource")
                .status(HttpStatus.FORBIDDEN.value())
                .timestamp(Instant.now())
                .path(request.getRequestURI())
                .build();
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    /**
     * Handle database constraint violations.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(
            DataIntegrityViolationException ex, 
            HttpServletRequest request) {
        
        log.error("Data integrity violation: {}", ex.getMessage());
        
        ErrorResponse response = ErrorResponse.builder()
                .errorId(UUID.randomUUID().toString())
                .errorCode("DATA_INTEGRITY_VIOLATION")
                .message("Database constraint violation occurred")
                .status(HttpStatus.CONFLICT.value())
                .timestamp(Instant.now())
                .path(request.getRequestURI())
                .build();
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    /**
     * Handle 404 when no handler is found.
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoHandlerFound(
            NoHandlerFoundException ex, 
            HttpServletRequest request) {
        
        log.warn("No handler found for: {} {}", ex.getHttpMethod(), ex.getRequestURL());
        
        ErrorResponse response = ErrorResponse.builder()
                .errorId(UUID.randomUUID().toString())
                .errorCode("ENDPOINT_NOT_FOUND")
                .message(String.format("Endpoint not found: %s %s", 
                        ex.getHttpMethod(), ex.getRequestURL()))
                .status(HttpStatus.NOT_FOUND.value())
                .timestamp(Instant.now())
                .path(request.getRequestURI())
                .build();
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * Handle all other exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
            Exception ex, 
            HttpServletRequest request) {
        
        log.error("Unhandled exception: {}", ex.getMessage(), ex);
        
        String errorId = UUID.randomUUID().toString();
        
        ErrorResponse response = ErrorResponse.builder()
                .errorId(errorId)
                .errorCode("INTERNAL_SERVER_ERROR")
                .message("An unexpected error occurred. Please contact support with error ID: " + errorId)
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .timestamp(Instant.now())
                .path(request.getRequestURI())
                .build();
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}