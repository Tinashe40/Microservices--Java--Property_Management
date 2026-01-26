package com.proveritus.propertyservice.infrastructure.client;

import com.tinash.cloud.utility.dto.common.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @GetMapping("/api/users/{id}")
    UserDto getUserById(@PathVariable("id") String id);

    @GetMapping("/api/users")
    List<UserDto> getUsersByIds(@RequestParam("ids") List<String> ids);
}
