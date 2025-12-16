package com.proveritus.userservice.passwordManager.scheduler;

import com.proveritus.cloudutility.dto.UserDTO;
import com.proveritus.userservice.passwordManager.service.PasswordService;
import com.proveritus.userservice.userManager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PasswordExpirationScheduler {

    private final UserService userService;
    private final PasswordService passwordService;

    @Scheduled(cron = "0 15 19 * * ?")
    public void checkPasswordExpirations() {
        List<UserDTO> users = (List<UserDTO>) userService.findAll();
        for (UserDTO user : users) {
            passwordService.checkPasswordExpiration(user.getId());
        }
    }
}
