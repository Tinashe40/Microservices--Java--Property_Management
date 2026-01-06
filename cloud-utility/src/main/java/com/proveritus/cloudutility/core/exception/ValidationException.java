package com.proveritus.cloudutility.core.exception;

  import java.util.Collections;
  import java.util.List;
  import java.util.Map;

  /**
   * Validation exception for invalid input.
   */
  public class ValidationException extends BusinessException {
      
      private final Map<String, List<String>> fieldErrors;
      
      public ValidationException(String message) {
          this(message, Collections.emptyMap());
      }
      
      public ValidationException(String message, Map<String, List<String>> fieldErrors) {
          super(message, "VALIDATION_ERROR");
          this.fieldErrors = fieldErrors;
      }
      
      public Map<String, List<String>> getFieldErrors() {
          return fieldErrors;
      }
  }