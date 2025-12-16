package com.proveritus.userservice.userGroups.service.Impl;

import com.proveritus.cloudutility.jpa.DomainServiceImpl;
import com.proveritus.userservice.userGroups.domain.UserGroup;
import com.proveritus.userservice.userGroups.domain.UserGroupRepository;
import com.proveritus.userservice.userGroups.dto.UserGroupDTO;
import com.proveritus.userservice.userGroups.mapper.UserGroupMapper;
import com.proveritus.userservice.userGroups.service.UserGroupService;
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
