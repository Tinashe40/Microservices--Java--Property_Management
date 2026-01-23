package com.tinash.cloud.utility.password.client;

import com.tinash.cloud.utility.password.dto.PasswordPolicyDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "user-service")
public interface PasswordPolicyClient {

    @GetMapping("/api/password-policy")
    PasswordPolicyDto getPasswordPolicy();
}
