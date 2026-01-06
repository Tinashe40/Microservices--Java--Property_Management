package com.proveritus.userservice.domain.model.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PasswordPolicy {
    private int minLength;
    private int maxLength;
    private boolean requiresUppercase;
    private boolean requiresLowercase;
    private boolean requiresNumber;
    private boolean requiresSpecialChar;

    public boolean isValid(Password password) {
        // TODO: Implement password policy validation
        return true;
    }
}
