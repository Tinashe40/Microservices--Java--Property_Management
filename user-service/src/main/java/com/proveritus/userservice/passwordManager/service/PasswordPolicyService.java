package com.proveritus.userservice.passwordManager.service;


import com.proveritus.cloudutility.password.dto.PasswordPolicyDTO;

public interface PasswordPolicyService {

    PasswordPolicyDTO getPasswordPolicy();

    PasswordPolicyDTO updatePasswordPolicy(PasswordPolicyDTO passwordPolicyDTO);
}
