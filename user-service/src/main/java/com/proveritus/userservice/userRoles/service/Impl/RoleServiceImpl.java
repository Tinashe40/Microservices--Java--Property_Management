package com.proveritus.userservice.userRoles.service.Impl;

import com.proveritus.cloudutility.jpa.DomainServiceImpl;
import com.proveritus.userservice.userRoles.domain.Role;
import com.proveritus.userservice.userRoles.domain.RoleRepository;
import com.proveritus.userservice.userRoles.dto.RoleDTO;
import com.proveritus.userservice.userRoles.mapper.RoleMapper;
import com.proveritus.userservice.userRoles.service.RoleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleServiceImpl extends DomainServiceImpl<Role, RoleDTO, RoleDTO, RoleDTO> implements RoleService {

    private final RoleRepository repository;

    public RoleServiceImpl(RoleRepository repository,
                           RoleMapper mapper) {
        super(repository, mapper);
        this.repository = repository;
    }

    @Override
    public Class<Role> getEntityClass() {
        return Role.class;
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
