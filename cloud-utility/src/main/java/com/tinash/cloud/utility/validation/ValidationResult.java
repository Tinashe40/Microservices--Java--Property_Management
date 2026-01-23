package com.tinash.cloud.utility.validation;

import java.util.*;

/**
 * Validation result container.
 */
public class ValidationResult {
    
    private final Map<String, List<String>> errors;
    
    private ValidationResult(Map<String, List<String>> errors) {
        this.errors = errors;
    }
    
    public static ValidationResult valid() {
        return new ValidationResult(Collections.emptyMap());
    }
    
    public static ValidationResult invalid(String field, String message) {
        Map<String, List<String>> errors = new HashMap<>();
        errors.put(field, Collections.singletonList(message));
        return new ValidationResult(errors);
    }
    
    public static ValidationResult invalid(Map<String, List<String>> errors) {
        return new ValidationResult(errors);
    }
    
    public boolean isValid() {
        return errors.isEmpty();
    }
    
    public Map<String, List<String>> getErrors() {
        return Collections.unmodifiableMap(errors);
    }
    
    public ValidationResult merge(ValidationResult other) {
        if (this.isValid()) {
            return other;
        }
        if (other.isValid()) {
            return this;
        }
        
        Map<String, List<String>> merged = new HashMap<>(this.errors);
        other.errors.forEach((field, messages) -> {
            merged.computeIfAbsent(field, k -> new ArrayList<>()).addAll(messages);
        });
        
        return new ValidationResult(merged);
    }
}
