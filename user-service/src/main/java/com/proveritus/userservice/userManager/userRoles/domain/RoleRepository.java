package com.proveritus.userservice.userManager.userRoles.domain;

import com.proveritus.cloudutility.jpa.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends BaseDao<Role, Long> {
    Optional<Role> findByName(String name);
}
