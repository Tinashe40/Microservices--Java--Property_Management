package com.proveritus.userservice.passwordManager.service;

import com.tinash.cloud.utility.password.dto.PasswordPolicyDTO;

public interface PasswordPolicyService {

    PasswordPolicyDTO getPasswordPolicy();

    PasswordPolicyDTO updatePasswordPolicy(PasswordPolicyDTO passwordPolicyDTO);
}
