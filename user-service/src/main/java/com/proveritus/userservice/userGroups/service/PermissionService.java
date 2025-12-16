package com.proveritus.userservice.userGroups.service;

import com.proveritus.cloudutility.jpa.DomainService;
import com.proveritus.userservice.userGroups.domain.Permission;
import com.proveritus.userservice.userGroups.dto.PermissionDTO;

public interface PermissionService extends DomainService<Permission, PermissionDTO, PermissionDTO, PermissionDTO> {
    void deleteById(Long id);

}
