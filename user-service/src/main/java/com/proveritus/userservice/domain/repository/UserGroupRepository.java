package com.proveritus.userservice.domain.repository;

import com.tinash.cloud.utility.jpa.BaseDao;
import com.proveritus.userservice.domain.model.usergroup.UserGroup;
import com.tinash.cloud.utility.jpa.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserGroupRepository extends BaseRepository<UserGroup, Long> {
    Optional<UserGroup> findByName(String name);

    Set<UserGroup> findByNameIn(Set<String> names);

    @Override
    @EntityGraph(attributePaths = "permissions")
    Page<UserGroup> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = "permissions")
    Optional<UserGroup> findById(Long id);
}