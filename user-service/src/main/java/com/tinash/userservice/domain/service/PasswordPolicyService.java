package com.tinash.userservice.domain.service;

import com.tinash.userservice.domain.model.password.Password;
import com.tinash.userservice.domain.model.password.PasswordPolicy;
import com.tinash.cloud.utility.jpa.BaseService;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

/**
 * Domain service for password policy enforcement.
 */
@Service
public class PasswordPolicyService extends BaseService<PasswordPolicy, String> {

    /**
     * Validates password against policy.
     */
    public boolean isValid(Password password, PasswordPolicy policy) {
        String plainText = password.getHashedValue();
        
        if (plainText.length() < policy.getMinLength() || 
            plainText.length() > policy.getMaxLength()) {
            return false;
        }
        
        if (policy.isRequiresUppercase() && !Pattern.compile("[A-Z]").matcher(plainText).find()) {
            return false;
        }
        
        if (policy.isRequiresLowercase() && !Pattern.compile("[a-z]").matcher(plainText).find()) {
            return false;
        }
        
        if (policy.isRequiresNumber() && !Pattern.compile("[0-9]").matcher(plainText).find()) {
            return false;
        }
        
        if (policy.isRequiresSpecialChar() && 
            !Pattern.compile("[!@#$%^&*(),.?\":{}|<>]" ).matcher(plainText).find()) {
            return false;
        }
        
        return true;
    }
}
