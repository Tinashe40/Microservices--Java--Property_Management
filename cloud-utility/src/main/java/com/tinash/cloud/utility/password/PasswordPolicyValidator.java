package com.tinash.cloud.utility.password;

import com.tinash.cloud.utility.constant.AppConstants;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Validates a password against a set of predefined policies.
 * This ensures strong and secure passwords are used within the application.
 */
@Component
public class PasswordPolicyValidator {

    // Minimum 8 characters, at least one uppercase letter, one lowercase letter, one digit, one special character.
    private static final String PASSWORD_PATTERN =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\[\]{};':\\"\\|,.<>/?]).{8,}$";

    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    /**
     * Validates if the given password meets the defined security policies.
     *
     * @param password The password string to validate.
     * @return true if the password is valid, false otherwise.
     */
    public boolean isValid(String password) {
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
        return AppConstants.PASSWORD_POLICY_MESSAGE;
    }
}
