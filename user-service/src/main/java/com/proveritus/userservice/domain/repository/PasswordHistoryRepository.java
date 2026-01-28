package com.proveritus.userservice.domain.repository;

import com.proveritus.userservice.domain.model.user.PasswordHistory;
import com.tinash.cloud.utility.jpa.BaseDao;
import com.tinash.cloud.utility.jpa.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PasswordHistoryRepository extends BaseRepository<PasswordHistory, String> {

    List<PasswordHistory> findByUserId(String userId);

    Page<PasswordHistory> findByUserId(String userId, Pageable pageable);

    List<PasswordHistory> findByUserIdOrderByCreatedDateAsc(String userId);
}