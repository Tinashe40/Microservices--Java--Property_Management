package com.proveritus.cloudutility.exception.technical;

import com.proveritus.cloudutility.exception.base.TechnicalException;

/**
 * Exception thrown for configuration-related errors.
 */
public class ConfigurationException extends TechnicalException {
    
    public ConfigurationException(String message) {
        super(message, "CONFIGURATION_ERROR");
    }
    
    public ConfigurationException(String message, Throwable cause) {
        super(message, "CONFIGURATION_ERROR", cause);
    }
}