package com.tinash.cloud.utility.password;

import com.tinash.cloud.utility.security.password.PasswordPolicy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
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
                regexBuilder.append("(?=.*[0-9])");
            }
            if (passwordPolicy.isRequireLowercase()) {
                regexBuilder.append("(?=.*[a-z])");
            }
            if (passwordPolicy.isRequireUppercase()) {
                regexBuilder.append("(?=.*[A-Z])");
            }
            if (passwordPolicy.isRequireSpecial()) {
                regexBuilder.append("(?=.*[@#$%^&+=])");
            }
            regexBuilder.append("(?=\\S+$)");
            regexBuilder.append(".{")
                    .append(passwordPolicy.getMinLength())
                    .append(",")
                    .append(passwordPolicy.getMaxLength())
                    .append("}$");

            this.pattern = Pattern.compile(regexBuilder.toString());

            // Dynamically build policy message
            StringBuilder messageBuilder = new StringBuilder("Password must be at least ")
                    .append(passwordPolicy.getMinLength()).append(" characters long");
            if (passwordPolicy.getMaxLength() > 0 && passwordPolicy.getMaxLength() != Integer.MAX_VALUE) {
                 messageBuilder.append(" and at most ").append(passwordPolicy.getMaxLength()).append(" characters long");
            }
            messageBuilder.append(", and contain:");
            if (passwordPolicy.isRequireUppercase()) {
                messageBuilder.append(" at least one uppercase letter,");
            }
            if (passwordPolicy.isRequireLowercase()) {
                messageBuilder.append(" at least one lowercase letter,");
            }
            if (passwordPolicy.isRequireDigit()) {
                messageBuilder.append(" at least one digit,");
            }
            if (passwordPolicy.isRequireSpecial()) {
                messageBuilder.append(" and at least one special character (e.g., @#$%^&+=).");
            }
            // Clean up trailing comma
            if (messageBuilder.charAt(messageBuilder.length() - 1) == ',') {
                messageBuilder.setLength(messageBuilder.length() - 1); // remove last comma
                messageBuilder.append(".");
            }
            this.policyMessage = messageBuilder.toString();
        }
    }


    /**
     * Validates if the given password meets the defined security policies.
     *
     * @param password The password string to validate.
     * @return true if the password is valid, false otherwise.
     */
    public boolean isValid(String password) {
        initializePolicy(); // Ensure policy is initialized
        if (password == null) {
            return false;
        }
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    /**
     * Returns a human-readable message describing the password policy.
     *
     * @return The password policy message.
     */
    public String getPolicyMessage() {
        initializePolicy(); // Ensure policy is initialized
        return policyMessage;
    }
}
