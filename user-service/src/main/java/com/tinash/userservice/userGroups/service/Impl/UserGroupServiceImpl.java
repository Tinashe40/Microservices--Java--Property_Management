package com.tinash.userservice.userGroups.service.Impl;

import com.tinash.cloud.utility.jpa.DomainServiceImpl;
import com.tinash.userservice.domain.model.usergroup.UserGroup;
import com.tinash.userservice.domain.repository.UserGroupRepository;
import com.tinash.userservice.userGroups.dto.UserGroupDTO;
import com.tinash.userservice.userGroups.mapper.UserGroupMapper;
import com.tinash.userservice.userGroups.service.UserGroupService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserGroupServiceImpl extends DomainServiceImpl<UserGroup, UserGroupDTO, UserGroupDTO, UserGroupDTO> implements UserGroupService {

    private final UserGroupRepository repository;

    public UserGroupServiceImpl(UserGroupRepository repository,
                           UserGroupMapper mapper) {
        super(repository, mapper);
        this.repository = repository;
    }

    @Override
    public Class<UserGroup> getEntityClass() {
        return UserGroup.class;
    }

    @Override
    public void deleteById(Long id) {
        super.deleteById(id);
    }
}
