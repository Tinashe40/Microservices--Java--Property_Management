package com.tinash.userservice.domain.repository;

import com.tinash.cloud.utility.jpa.BaseDao;
import com.tinash.userservice.domain.model.permission.Permission;
import com.tinash.cloud.utility.jpa.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface PermissionRepository extends BaseRepository<Permission, Long> {
    Optional<Permission> findByName(String name);
    Set<Permission> findByNameIn(Set<String> names);
}