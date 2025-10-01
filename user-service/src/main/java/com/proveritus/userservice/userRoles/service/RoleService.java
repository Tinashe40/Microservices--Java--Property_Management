package com.proveritus.userservice.userRoles.service;

import com.proveritus.cloudutility.jpa.DomainService;
import com.proveritus.userservice.userRoles.domain.Role;
import com.proveritus.userservice.userRoles.dto.RoleDTO;

public interface RoleService extends DomainService<Role, RoleDTO, RoleDTO, RoleDTO> {
    void deleteById(Long id);
}
