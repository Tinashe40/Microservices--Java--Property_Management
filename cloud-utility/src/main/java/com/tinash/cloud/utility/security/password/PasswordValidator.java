package com.tinash.cloud.utility.security.password;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class PasswordValidator {

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$");

    public boolean validate(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }
}
