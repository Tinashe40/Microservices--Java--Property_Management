package com.tinash.userservice.passwordManager.scheduler;

import com.tinash.cloud.utility.dto.UserDto;
import com.tinash.userservice.passwordManager.service.PasswordService;
import com.tinash.userservice.userManager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordExpirationScheduler {

    private final UserService userService;
    private final PasswordService passwordService;

    @Scheduled(cron = "0 15 19 * * ?")
    public void checkPasswordExpirations() {
        UserDto users = (UserDto) userService.findAll();
        for (UserDTO user : users) {
            passwordService.checkPasswordExpiration(user.getId());
        }
    }
}
