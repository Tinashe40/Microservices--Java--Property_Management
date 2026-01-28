package com.proveritus.userservice.passwordManager.dto;

import lombok.Data;

@Data
public class PasswordPolicyDto {
    private int minLength;
    private int maxLength;
    private boolean requiresUppercase;
    private boolean requiresLowercase;
    private boolean requiresNumber;
    private boolean requiresSpecialChar;
    private int passwordHistorySize;
    private int passwordExpirationDays;
}
