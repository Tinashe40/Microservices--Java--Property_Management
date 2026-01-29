package com.tinash.userservice.passwordManager;

import com.tinash.userservice.domain.model.password.PasswordPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.regex.Pattern;

/**
 * Validates a password against a set of predefined policies.
 * This ensures strong and secure passwords are used within the application.
 */
@Component
@RequiredArgsConstructor
public class PasswordPolicyValidator {

    private final PasswordPolicy passwordPolicy;
    private Pattern pattern;
    private String policyMessage;

    private void initializePolicy() {
        if (pattern == null) {
            StringBuilder regexBuilder = new StringBuilder();
            if (passwordPolicy.isRequireDigit()) {
                regexBuilder.append("(?=[0-9])");
            }
            if (passwordPolicy.isRequireLowercase()) {
                regexBuilder.append("(?=[a-z])");
            }
            if (passwordPolicy.isRequireUppercase()) {
                regexBuilder.append("(?=[A-Z])");
            }
            if (passwordPolicy.isRequireSpecial()) {
                regexBuilder.append("(?=[@#$%^&+=])");
            }
            regexBuilder.append("(?=\\S+$)");
            regexBuilder.append(".{")
                    .append(passwordPolicy.getMinLength())
                    .append(",")
                    .append(passwordPolicy.getMaxLength())
                    .append(