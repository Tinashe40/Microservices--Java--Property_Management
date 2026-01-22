package com.proveritus.userservice.passwordManager.service;

import com.tinash.cloud.utility.password.dto.ChangePasswordRequest;
import com.tinash.cloud.utility.password.dto.ResetPasswordRequest;

public interface PasswordService {

    void changePassword(ChangePasswordRequest changePasswordRequest);

    void checkPasswordExpiration(Long userId);

    void adminResetPassword(Long userId, ResetPasswordRequest request);
}
