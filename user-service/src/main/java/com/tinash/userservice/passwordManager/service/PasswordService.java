package com.tinash.userservice.passwordManager.service;

import com.tinash.userservice.passwordManager.dto.ChangePasswordRequest;
import com.tinash.userservice.passwordManager.dto.ResetPasswordRequest;

public interface PasswordService {

    void changePassword(ChangePasswordRequest changePasswordRequest);

    void checkPasswordExpiration(Long userId);

    void adminResetPassword(Long userId, ResetPasswordRequest request);
}
