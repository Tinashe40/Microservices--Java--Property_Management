package com.proveritus.propertyservice.client;

import com.proveritus.cloudutility.dto.UserDTO;
import com.proveritus.cloudutility.security.RemoteUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RemoteUserDetailsServiceImpl extends RemoteUserDetailsService {

    private final UserClient userClient;

    @Override
    public UserDTO getUserByUsername(String username) {
        return userClient.getUserByUsername(username);
    }
}
