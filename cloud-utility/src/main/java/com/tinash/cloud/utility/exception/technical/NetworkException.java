package com.tinash.cloud.utility.exception.technical;

import com.tinash.cloud.utility.exception.base.TechnicalException;

/**
 * Exception thrown for network-related errors.
 */

public class NetworkException extends TechnicalException {
    
    public NetworkException(String message, Throwable cause) {
        super(message, "NETWORK_ERROR", cause);
    }
}
