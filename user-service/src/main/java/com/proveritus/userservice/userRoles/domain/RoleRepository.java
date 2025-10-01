package com.proveritus.userservice.userRoles.domain;

import com.proveritus.cloudutility.jpa.BaseDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends BaseDao<Role, Long> {
    Optional<Role> findByName(String name);

    Set<Role> findByNameIn(Set<String> names);

    @Override
    @EntityGraph(attributePaths = "permissions")
    Page<Role> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = "permissions")
    Optional<Role> findById(Long id);
}
