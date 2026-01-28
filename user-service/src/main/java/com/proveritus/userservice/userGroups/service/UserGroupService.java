package com.proveritus.userservice.userGroups.service;

import com.tinash.cloud.utility.jpa.DomainService;
import com.proveritus.userservice.domain.model.usergroup.UserGroup;
import com.proveritus.userservice.userGroups.dto.UserGroupDTO;

public interface UserGroupService extends DomainService<UserGroup, UserGroupDTO, UserGroupDTO, UserGroupDTO> {
    void deleteById(Long id);
}
