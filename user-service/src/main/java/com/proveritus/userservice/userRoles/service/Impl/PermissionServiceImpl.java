package com.proveritus.userservice.userRoles.service.Impl;

import com.proveritus.cloudutility.jpa.DomainServiceImpl;
import com.proveritus.userservice.userRoles.domain.Permission;
import com.proveritus.userservice.userRoles.domain.PermissionRepository;
import com.proveritus.userservice.userRoles.dto.PermissionDTO;
import com.proveritus.userservice.userRoles.mapper.PermissionMapper;
import com.proveritus.userservice.userRoles.service.PermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PermissionServiceImpl extends DomainServiceImpl<Permission, PermissionDTO, PermissionDTO, PermissionDTO> implements PermissionService {

    public PermissionServiceImpl(PermissionRepository repository, PermissionMapper mapper) {
        super(repository, mapper);
    }

    @Override
    public Class<Permission> getEntityClass() {
        return Permission.class;
    }
}
