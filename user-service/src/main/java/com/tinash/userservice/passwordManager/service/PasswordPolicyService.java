package com.tinash.userservice.passwordManager.service;

import com.tinash.userservice.passwordManager.dto.PasswordPolicyDto;
import com.tinash.cloud.utility.jpa.BaseService;
import com.tinash.cloud.utility.security.password.PasswordPolicy;

public interface PasswordPolicyService extends BaseService<PasswordPolicyDto, PasswordPolicy> {

    PasswordPolicyDto getPasswordPolicy();

    PasswordPolicyDto updatePasswordPolicy(PasswordPolicyDto passwordPolicyDto);
}
