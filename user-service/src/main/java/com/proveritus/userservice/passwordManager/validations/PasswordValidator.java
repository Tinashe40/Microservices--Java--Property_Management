package com.proveritus.userservice.passwordManager.validations;

import com.proveritus.cloudutility.exception.InvalidPasswordException;
import com.proveritus.cloudutility.passwordManager.dto.PasswordPolicyDTO;
import org.springframework.stereotype.Component;

@Component
public class PasswordValidator {

    public void validate(String password, PasswordPolicyDTO passwordPolicyDTO) {
        if (password.length() < passwordPolicyDTO.getMinLength()) {
            throw new InvalidPasswordException("Password must be at least " + passwordPolicyDTO.getMinLength() + " characters long");
        }

        if (password.length() > passwordPolicyDTO.getMaxLength()) {
            throw new InvalidPasswordException("Password must be at most " + passwordPolicyDTO.getMaxLength() + " characters long");
        }

        if (passwordPolicyDTO.isRequiresUppercase() && !password.matches(".*[A-Z].*")) {
            throw new InvalidPasswordException("Password must contain at least one uppercase letter");
        }

        if (passwordPolicyDTO.isRequiresLowercase() && !password.matches(".*[a-z].*")) {
            throw new InvalidPasswordException("Password must contain at least one lowercase letter");
        }

        if (passwordPolicyDTO.isRequiresNumber() && !password.matches(".*[0-9].*")) {
            throw new InvalidPasswordException("Password must contain at least one number");
        }

        if (passwordPolicyDTO.isRequiresSpecialChar() && !password.matches(".*\\p{Punct}.*")) {
            throw new InvalidPasswordException("Password must contain at least one special character");
        }
    }
}
