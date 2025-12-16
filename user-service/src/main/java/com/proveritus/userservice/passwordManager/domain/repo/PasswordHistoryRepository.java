package com.proveritus.userservice.passwordManager.domain.repo;

import com.proveritus.userservice.passwordManager.domain.model.PasswordHistory;
import com.proveritus.cloudutility.jpa.BaseDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PasswordHistoryRepository extends BaseDao<PasswordHistory, Long> {

    List<PasswordHistory> findByUserId(Long userId);

    Page<PasswordHistory> findByUserId(Long userId, Pageable pageable);

    List<PasswordHistory> findByUserIdOrderByCreatedDateAsc(Long userId);
}
