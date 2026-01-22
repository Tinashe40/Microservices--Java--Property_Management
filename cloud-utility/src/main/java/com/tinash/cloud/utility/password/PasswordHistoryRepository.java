package com.tinash.cloud.utility.password;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for managing {@link PasswordHistory} entities.
 */
@Repository
public interface PasswordHistoryRepository extends JpaRepository<PasswordHistory, Long> {

    /**
     * Finds the last N passwords for a given user, ordered by creation date descending.
     * @param userId The ID of the user.
     * @param limit The maximum number of historical passwords to retrieve.
     * @return A list of PasswordHistory entries.
     */
    List<PasswordHistory> findTopNByUserIdOrderByCreatedAtDesc(Long userId, int limit);

    /**
     * Finds all historical passwords for a given user.
     * @param userId The ID of the user.
     * @return A list of PasswordHistory entries.
     */
    List<PasswordHistory> findByUserIdOrderByCreatedAtDesc(Long userId);
}
