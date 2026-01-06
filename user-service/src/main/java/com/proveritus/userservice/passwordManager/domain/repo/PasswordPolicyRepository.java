package com.proveritus.userservice.passwordManager.domain.repo;

import com.proveritus.userservice.passwordManager.domain.model.PasswordPolicy;
import com.proveritus.cloudutility.jpa.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordPolicyRepository extends BaseDao<PasswordPolicy, String> {
    Optional<PasswordPolicy> findFirstByOrderByIdAsc();
}
