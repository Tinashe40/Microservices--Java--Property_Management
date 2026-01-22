package com.proveritus.userservice.domain.repository;

import com.tinash.cloud.utility.jpa.BaseDao;
import com.proveritus.userservice.domain.model.permission.Permission;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface PermissionRepository extends BaseDao<Permission, Long> {
    Optional<Permission> findByName(String name);
    Set<Permission> findByNameIn(Set<String> names);
}