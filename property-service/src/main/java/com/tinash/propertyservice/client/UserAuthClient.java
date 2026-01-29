package com.tinash.propertyservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service")
public interface UserAuthClient {

    @PostMapping("/auth/is-blacklisted")
    ResponseEntity<Boolean> isTokenBlacklisted(@RequestBody String token);
}
