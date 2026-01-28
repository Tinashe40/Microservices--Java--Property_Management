package com.proveritus.userservice.passwordManager.service;

import com.proveritus.userservice.passwordManager.dto.ChangePasswordRequest;
import com.proveritus.userservice.passwordManager.dto.ResetPasswordRequest;

public interface PasswordService {

    void changePassword(ChangePasswordRequest changePasswordRequest);

    void checkPasswordExpiration(Long userId);

    void adminResetPassword(Long userId, ResetPasswordRequest request);
}
