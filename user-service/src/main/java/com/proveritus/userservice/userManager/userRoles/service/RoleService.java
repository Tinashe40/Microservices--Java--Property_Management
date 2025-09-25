package com.proveritus.userservice.userManager.userRoles.service;

import com.proveritus.cloudutility.jpa.DomainService;
import com.proveritus.userservice.userManager.userRoles.domain.Role;
import com.proveritus.userservice.userManager.userRoles.dto.CreateRoleDTO;
import com.proveritus.userservice.userManager.userRoles.dto.RoleDTO;
import com.proveritus.userservice.userManager.userRoles.dto.UpdateRoleDTO;

public interface RoleService extends DomainService<Role, CreateRoleDTO, UpdateRoleDTO, RoleDTO> {
    void deleteById(Long id);
}
