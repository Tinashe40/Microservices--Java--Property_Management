package com.proveritus.cloudutility.jpa.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface QueryRepository<T, ID> extends Repository<T, ID>, JpaSpecificationExecutor<T> {

    Optional<T> findById(ID id);
    Page<T> findById(Pageable pageable, ID id);
    Page<T> findAll(Pageable pageable);
    List<T> findAll();
}