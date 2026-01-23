package com.tinash.cloud.utility.exception.business;

import com.tinash.cloud.utility.exception.base.BusinessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyExistsException extends BusinessException {

    public UserAlreadyExistsException(String message) {
        super(message, "USER_ALREADY_EXISTS");
    }
}
