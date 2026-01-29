package com.tinash.userservice.passwordManager.service;

import com.tinash.userservice.passwordManager.dto.ChangePasswordRequest;

import com.tinash.userservice.passwordManager.dto.PasswordPolicyDto;
import com.tinash.userservice.passwordManager.dto.ResetPasswordRequest;
import com.tinash.userservice.domain.model.password.PasswordHistory;
import com.tinash.userservice.domain.repository.PasswordHistoryRepository;
import com.tinash.userservice.passwordManager.validations.PasswordValidator;
import com.tinash.userservice.userManager.service.UserService;
import com.tinash.cloud.utility.dto.common.UserDto;
import com.tinash.cloud.utility.exception.business.InvalidPasswordException;
import com.tinash.cloud.utility.security.password.CustomPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PasswordServiceImpl implements PasswordService {

    private final PasswordPolicyService passwordPolicyService;
    private final PasswordHistoryRepository passwordHistoryRepository;
    private UserService userService;
    private final CustomPasswordEncoder customPasswordEncoder;
    private final PasswordValidator passwordValidator;

    @Autowired
    public PasswordServiceImpl(PasswordPolicyService passwordPolicyService,
                               PasswordHistoryRepository passwordHistoryRepository,
                               @Lazy UserService userService,
                               CustomPasswordEncoder customPasswordEncoder,
                               PasswordValidator passwordValidator) {
        this.passwordPolicyService = passwordPolicyService;
        this.passwordHistoryRepository = passwordHistoryRepository;
        this.userService = userService;
        this.customPasswordEncoder=customPasswordEncoder;
        this.passwordValidator = passwordValidator;
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest changePasswordRequest) {
        UserDto user = userService.getCurrentUser();
        if (!customPasswordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new InvalidPasswordException("Invalid old password");
        }
        PasswordPolicyDto passwordPolicyDto = passwordPolicyService.getPasswordPolicy();
        updateUserPassword(user, changePasswordRequest.getNewPassword(), passwordPolicyDto);
    }

    @Override
    @Transactional
    public void adminResetPassword(Long userId, ResetPasswordRequest request) {
        UserDto user = userService.findById(userId);
        PasswordPolicyDTO passwordPolicyDTO = passwordPolicyService.getPasswordPolicy();
        updateUserPassword(user, request.getNewPassword(), passwordPolicyDTO);
    }

    private void updateUserPassword(UserDto user, String newPassword, PasswordPolicyDTO passwordPolicyDTO) {
        passwordValidator.validate(newPassword, passwordPolicyDTO);

        if (passwordPolicyDTO.getPasswordHistorySize() > 0) {
            Pageable pageable = PageRequest.of(0, passwordPolicyDTO.getPasswordHistorySize(), Sort.by(Sort.Direction.DESC, "createdDate"));
            List<PasswordHistory> passwordHistory = passwordHistoryRepository.findByUserId(user.getId(), pageable).getContent();

            for (PasswordHistory history : passwordHistory) {
                if (customPasswordEncoder.matches(newPassword, history.getPassword())) {
                    throw new InvalidPasswordException("New password cannot be the same as any of the last " +
                            passwordPolicyDTO.getPasswordHistorySize() + " passwords");
                }
            }
        }

        user.setPassword(customPasswordEncoder.encode(newPassword));
        user.setPasswordLastChanged(LocalDateTime.now());

        UpdateUserDto updateUserDto = new UpdateUserDto();
        updateUserDto.setId(user.getId());
        updateUserDto.setUsername(user.getUsername());
        updateUserDto.setEmail(user.getEmail());
        updateUserDto.setFirstName(user.getFirstName());
        updateUserDto.setLastName(user.getLastName());

        userService.update(updateUserDto);

        PasswordHistory newPasswordHistory = new PasswordHistory();
        newPasswordHistory.setUserId(user.getId());
        newPasswordHistory.setPassword(user.getPassword());
        passwordHistoryRepository.save(newPasswordHistory);

        // Pruning logic
        if (passwordPolicyDTO.getPasswordHistorySize() > 0) {
            List<PasswordHistory> allHistory = passwordHistoryRepository.findByUserIdOrderByCreatedDateAsc(user.getId());
            if (allHistory.size() > passwordPolicyDTO.getPasswordHistorySize()) {
                int toDeleteCount = allHistory.size() - passwordPolicyDTO.getPasswordHistorySize();
                List<PasswordHistory> toDelete = allHistory.subList(0, toDeleteCount);
                passwordHistoryRepository.deleteAll(toDelete);
            }
        }
    }

    @Override
    public void checkPasswordExpiration(Long userId) {
        UserDto user = userService.findById(userId);
        PasswordPolicyDTO passwordPolicyDTO = passwordPolicyService.getPasswordPolicy();
        if (passwordPolicyDTO.getPasswordExpirationDays() > 0) {
            LocalDateTime passwordLastChanged = user.getPasswordLastChanged();
            if (passwordLastChanged != null &&
                    passwordLastChanged.plusDays(passwordPolicyDTO.getPasswordExpirationDays()).isBefore(LocalDateTime.now())) {
                userService.deactivateUser(userId);
            }
        }
    }
}
