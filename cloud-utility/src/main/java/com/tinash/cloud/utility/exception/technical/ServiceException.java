package com.tinash.cloud.utility.exception.technical;

import com.tinash.cloud.utility.exception.base.TechnicalException;

public class ServiceException extends TechnicalException {
    public ServiceException(String message) {
        super(message, "SERVICE_ERROR");
    }
}
