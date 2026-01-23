package com.tinash.cloud.utility.exception.business;

import com.tinash.cloud.utility.exception.base.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidPasswordException extends BusinessException {

    public InvalidPasswordException(String message) {
        super(message, "INVALID_PASSWORD");
    }
}
