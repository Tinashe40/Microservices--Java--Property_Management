package com.proveritus.cloudutility.client;

import com.proveritus.cloudutility.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/api/users/me")
    UserDTO getCurrentUser();

    @GetMapping("/api/users/{userId}")
    UserDTO getUserById(@PathVariable("userId") Long userId);

    @GetMapping("/api/users")
    List<UserDTO> getAllUsers();

    @PutMapping("/api/users")
    UserDTO updateUser(@RequestBody UserDTO userDTO);

    @PostMapping("/api/users/{userId}/expire-password")
    void expirePassword(@PathVariable("userId") Long userId);
}


