package com.proveritus.userservice.domain.repository;

import com.proveritus.userservice.domain.model.user.PasswordPolicy;
import com.tinash.cloud.utility.jpa.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordPolicyRepository extends BaseDao<PasswordPolicy, String> {
    Optional<PasswordPolicy> findFirstByOrderByIdAsc();
}