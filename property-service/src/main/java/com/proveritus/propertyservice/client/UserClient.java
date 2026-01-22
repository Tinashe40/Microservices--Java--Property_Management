package com.proveritus.propertyservice.client;

import com.tinash.cloud.utility.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@FeignClient(name = "user-service")
public interface UserClient {

    @GetMapping("/users/{id}")
    UserDto getUserById(@PathVariable("id") Long id);

    @GetMapping("/users/by-username")
    UserDto getUserByUsername(@RequestParam("username") String username);

    @PostMapping("/users/by-ids")
    List<UserDto> getUsersByIds(@RequestBody List<Long> ids);
}
