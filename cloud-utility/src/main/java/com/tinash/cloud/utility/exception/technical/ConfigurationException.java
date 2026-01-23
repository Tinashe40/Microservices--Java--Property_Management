package com.tinash.cloud.utility.exception.technical;

import com.tinash.cloud.utility.exception.base.TechnicalException;

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
