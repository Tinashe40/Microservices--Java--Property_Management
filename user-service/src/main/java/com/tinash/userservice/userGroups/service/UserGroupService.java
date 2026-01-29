package com.tinash.userservice.userGroups.service;

import com.tinash.cloud.utility.jpa.DomainService;
import com.tinash.userservice.domain.model.usergroup.UserGroup;
import com.tinash.userservice.userGroups.dto.UserGroupDTO;

public interface UserGroupService extends DomainService<UserGroup, UserGroupDTO, UserGroupDTO, UserGroupDTO> {
    void deleteById(Long id);
}
