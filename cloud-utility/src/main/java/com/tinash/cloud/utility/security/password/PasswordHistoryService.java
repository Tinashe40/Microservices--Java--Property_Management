package com.tinash.cloud.utility.security.password;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PasswordHistoryService {

    private final List<String> passwordHistory = new ArrayList<>();
    public void addPasswordToHistory(String password) {
        passwordHistory.add(password);
    }
    public boolean isPasswordInHistory(String password) {
        return passwordHistory.contains(password);
    }
}
