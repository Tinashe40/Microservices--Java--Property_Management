package com.tinash.cloud.utility.security.password;

import org.springframework.stereotype.Component;

@Component
public class PasswordStrengthCalculator {

    public int calculateStrength(String password) {
        int score = 0;
        if (password.length() >= 8) {
            score++;
        }
        if (password.matches(".*[0-9].*")) {
            score++;
        }
        if (password.matches(".*[a-z].*")) {
            score++;
        }
        if (password.matches(".*[A-Z].*")) {
            score++;
        }
        if (password.matches(".*[@#$%^&+=].*")) {
            score++;
        }
        return score;
    }
}
