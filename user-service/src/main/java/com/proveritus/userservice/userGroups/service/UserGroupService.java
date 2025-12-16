package com.proveritus.userservice.userGroups.service;

import com.proveritus.cloudutility.jpa.DomainService;
import com.proveritus.userservice.userGroups.domain.UserGroup;
import com.proveritus.userservice.userGroups.dto.UserGroupDTO;

public interface UserGroupService extends DomainService<UserGroup, UserGroupDTO, UserGroupDTO, UserGroupDTO> {
    void deleteById(Long id);
}
