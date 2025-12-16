package com.proveritus.userservice.passwordManager.service;

import com.proveritus.cloudutility.passwordManager.dto.PasswordPolicyDTO;

public interface PasswordPolicyService {

    PasswordPolicyDTO getPasswordPolicy();

    PasswordPolicyDTO updatePasswordPolicy(PasswordPolicyDTO passwordPolicyDTO);
}
