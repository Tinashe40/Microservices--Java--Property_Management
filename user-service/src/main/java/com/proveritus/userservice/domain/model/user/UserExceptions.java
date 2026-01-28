package com.proveritus.userservice.domain.model.user;

import com.tinash.cloud.utility.exception.base.BusinessException;

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
