package com.proveritus.propertyservice.client;

import com.tinash.cloud.utility.dto.UserDto;
import com.tinash.cloud.utility.security.RemoteUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RemoteUserDetailsServiceImpl extends RemoteUserDetailsService {

    private final UserClient userClient;

    @Override
    public UserDto getUserByUsername(String username) {
        return userClient.getUserByUsername(username);
    }
}
