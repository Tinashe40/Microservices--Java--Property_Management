package com.tinash.cloud.utility.exception.technical;

import com.tinash.cloud.utility.exception.base.TechnicalException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
public class UserServiceNotAvailableException extends TechnicalException {

    public UserServiceNotAvailableException(String message) {
        super(message, "USER_SERVICE_NOT_AVAILABLE");
    }

    public UserServiceNotAvailableException(String message, Throwable cause) {
        super(message, "USER_SERVICE_NOT_AVAILABLE", cause);
    }
}
