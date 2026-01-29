package com.tinash.userservice.passwordManager;

import com.tinash.userservice.passwordManager.service.PasswordPolicyService;
import com.tinash.userservice.domain.model.user.User;
import com.tinash.userservice.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordExpirationJob {

    private final UserRepository userRepository;
    private final PasswordPolicyService passwordPolicyService;

    @Scheduled(cron = "05 15 19 * * ?")
    @Transactional
    public void expirePasswords() {
        log.info("Running password expiration job.");
        int passwordExpirationDays = passwordPolicyService.getPasswordPolicy().getPasswordExpirationDays();
        if (passwordExpirationDays <= 0) {
            return;
        }

        Page<User> users = userRepository.findAll(PageRequest.of(0, passwordExpirationDays, Sort.unsorted()));
        for (User user : users) {
            if (user.getPasswordLastChanged() != null && user.isCredentialsNonExpired()) {
                if (user.getPasswordLastChanged().plusDays(passwordExpirationDays).isBefore(LocalDateTime.now())) {
                    user.expireCredentials(); // Use the domain method
                    userRepository.save(user);
                    log.info("Expired password for user: {}", user.getUsername());
                }
            }
        }
    }
}
