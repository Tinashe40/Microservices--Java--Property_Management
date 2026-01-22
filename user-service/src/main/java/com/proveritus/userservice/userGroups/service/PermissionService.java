package com.proveritus.userservice.userGroups.service;

import com.tinash.cloud.utility.jpa.DomainService;
import com.proveritus.userservice.userGroups.domain.Permission;
import com.proveritus.userservice.userGroups.dto.PermissionDTO;

public interface PermissionService extends DomainService<Permission, PermissionDTO, PermissionDTO, PermissionDTO> {
    void deleteById(Long id);

}
