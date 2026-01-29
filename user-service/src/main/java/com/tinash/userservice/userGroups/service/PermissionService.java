package com.tinash.userservice.userGroups.service;

import com.tinash.cloud.utility.jpa.DomainService;
import com.tinash.userservice.domain.model.permission.Permission;
import com.tinash.userservice.userGroups.dto.PermissionDTO;

public interface PermissionService extends DomainService<Permission, PermissionDTO, PermissionDTO, PermissionDTO> {
    void deleteById(Long id);

}
