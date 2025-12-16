package com.proveritus.cloudutility.service;

import com.proveritus.cloudutility.dto.UserDTO;
import com.proveritus.cloudutility.security.CustomPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class BaseService {

    public UserDTO getCurrentUser() {
        CustomPrincipal principal = (CustomPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDTO userDTO = new UserDTO();
        userDTO.setId(principal.getId());
        userDTO.setUsername(principal.getUsername());
        userDTO.setRoles(principal.getAuthorities().stream().map(a -> a.getAuthority()).collect(Collectors.toSet()));
        return userDTO;
    }
}