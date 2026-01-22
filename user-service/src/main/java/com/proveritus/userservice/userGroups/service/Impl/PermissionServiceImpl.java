package com.proveritus.userservice.userGroups.service.Impl;

import com.tinash.cloud.utility.jpa.DomainServiceImpl;
import com.proveritus.userservice.userGroups.domain.Permission;
import com.proveritus.userservice.userGroups.domain.PermissionRepository;
import com.proveritus.userservice.userGroups.dto.PermissionDTO;
import com.proveritus.userservice.userGroups.mapper.PermissionMapper;
import com.proveritus.userservice.userGroups.service.PermissionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PermissionServiceImpl extends DomainServiceImpl<Permission, PermissionDTO, PermissionDTO, PermissionDTO> implements PermissionService {

    private final PermissionRepository repository;

    public PermissionServiceImpl(PermissionRepository repository,
                                 PermissionMapper mapper) {
        super(repository,mapper);
        this.repository = repository;
    }

    @Override
    public Class<Permission> getEntityClass() {
        return Permission.class;
    }

    @Override
    public void deleteById(Long id) {
        super.deleteById(id);
    }
}
