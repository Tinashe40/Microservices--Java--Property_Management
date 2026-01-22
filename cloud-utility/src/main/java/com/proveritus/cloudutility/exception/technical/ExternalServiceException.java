package com.proveritus.cloudutility.exception.technical;

import com.tinash.cloud.utility.exception.base.TechnicalException;

/**
 * Exception thrown when external service call fails.
 */
public class ExternalServiceException extends TechnicalException {
    
    private final String serviceName;
    
    public ExternalServiceException(String serviceName, String message, Throwable cause) {
        super(
            String.format("External service '%s' failed: %s", serviceName, message),
            "EXTERNAL_SERVICE_ERROR",
            cause
        );
        this.serviceName = serviceName;
    }
    
    public String getServiceName() {
        return serviceName;
    }
    
    @Override
    public int getHttpStatus() {
        return 503; // Service Unavailable
    }
}