package com.proveritus.userservice.userRoles.domain;

import com.proveritus.cloudutility.jpa.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface PermissionRepository extends BaseDao<Permission, Long> {
    Optional<Permission> findByName(String name);
    Set<Permission> findByNameIn(Set<String> names);
}
