package com.proveritus.userservice.userManager.userRoles.service;

import com.proveritus.cloudutility.jpa.DomainServiceImpl;
import com.proveritus.userservice.userManager.userRoles.domain.Role;
import com.proveritus.userservice.userManager.userRoles.domain.RoleRepository;
import com.proveritus.userservice.userManager.userRoles.dto.CreateRoleDTO;
import com.proveritus.userservice.userManager.userRoles.dto.RoleDTO;
import com.proveritus.userservice.userManager.userRoles.dto.UpdateRoleDTO;
import com.proveritus.userservice.userManager.userRoles.mapper.RoleMapper;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends DomainServiceImpl<Role, CreateRoleDTO, UpdateRoleDTO, RoleDTO> implements RoleService {

    public RoleServiceImpl(RoleRepository repository, RoleMapper mapper) {
        super(repository, mapper);
    }

    @Override
    public Class<Role> getEntityClass() {
        return Role.class;
    }
}
