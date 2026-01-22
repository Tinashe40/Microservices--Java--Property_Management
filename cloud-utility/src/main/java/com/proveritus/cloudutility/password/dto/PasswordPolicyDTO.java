package com.proveritus.cloudutility.password.dto;

import lombok.Data;

@Data
public class PasswordPolicyDTO {
    private int minLength;
    private int maxLength;
    private boolean requiresUppercase;
    private boolean requiresLowercase;
    private boolean requiresNumber;
    private boolean requiresSpecialChar;
    private int passwordHistorySize;
    private int passwordExpirationDays;
}
