package com.proveritus.userservice.userRoles.service;

import com.proveritus.cloudutility.jpa.DomainService;
import com.proveritus.userservice.userRoles.domain.Permission;
import com.proveritus.userservice.userRoles.dto.PermissionDTO;

public interface PermissionService extends DomainService<Permission, PermissionDTO, PermissionDTO, PermissionDTO> {
    void deleteById(Long id);

}
