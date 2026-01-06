package com.proveritus.userservice.domain.model.user;

import com.proveritus.cloudutility.core.exception.BusinessException;

public class InvalidPasswordException extends BusinessException {
    public InvalidPasswordException(String message) {
        super(message, "INVALID_PASSWORD");
    }
}

class PasswordPolicyViolationException extends BusinessException {
    public PasswordPolicyViolationException(String message) {
        super(message, "PASSWORD_POLICY_VIOLATION");
    }
}
