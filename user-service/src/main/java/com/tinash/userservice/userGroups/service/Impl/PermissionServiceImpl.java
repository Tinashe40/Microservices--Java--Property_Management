package com.tinash.userservice.userGroups.service.Impl;

import com.tinash.cloud.utility.jpa.DomainServiceImpl;
import com.tinash.userservice.domain.model.permission.Permission;
import com.tinash.userservice.domain.repository.PermissionRepository;
import com.tinash.userservice.userGroups.dto.PermissionDTO;
import com.tinash.userservice.userGroups.mapper.PermissionMapper;
import com.tinash.userservice.userGroups.service.PermissionService;
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
