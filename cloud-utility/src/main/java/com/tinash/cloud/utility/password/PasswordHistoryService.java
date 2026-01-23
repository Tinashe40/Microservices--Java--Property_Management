package com.tinash.cloud.utility.password;

import com.tinash.cloud.utility.security.password.CustomPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for managing user password history.
 * Enforces password reuse policy by comparing new passwords against a user's
 * historical passwords.
 */
@Service
@Transactional(readOnly = true)
public class PasswordHistoryService {

    private final PasswordHistoryRepository passwordHistoryRepository;
    private final CustomPasswordEncoder customPasswordEncoder;

    // Number of previous passwords to check against (configurable)
    private static final int HISTORY_LIMIT = 5;

    public PasswordHistoryService(PasswordHistoryRepository passwordHistoryRepository,
                                  CustomPasswordEncoder customPasswordEncoder) {
        this.passwordHistoryRepository = passwordHistoryRepository;
        this.customPasswordEncoder = customPasswordEncoder;
    }

    /**
     * Checks if the new password has been used recently by the user.
     *
     * @param userId The ID of the user.
     * @param newPassword The new password to check.
     * @return true if the new password has been used recently, false otherwise.
     */
    public boolean hasPasswordBeenUsedRecently(Long userId, String newPassword) {
        List<PasswordHistory> history = passwordHistoryRepository.findTopNByUserIdOrderByCreatedAtDesc(userId, HISTORY_LIMIT);
        for (PasswordHistory entry : history) {
            if (customPasswordEncoder.matches(newPassword, entry.getHashedPassword())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Saves a new hashed password to the user's history.
     * This should be called after a password change or creation.
     *
     * @param userId The ID of the user.
     * @param hashedPassword The new hashed password.
     */
    @Transactional
    public void savePasswordToHistory(Long userId, String hashedPassword) {
        PasswordHistory newEntry = new PasswordHistory();
        newEntry.setUserId(userId);
        newEntry.setHashedPassword(hashedPassword);
        passwordHistoryRepository.save(newEntry);

        // Optionally, prune old entries to keep history limit
        List<PasswordHistory> history = passwordHistoryRepository.findByUserIdOrderByCreatedAtDesc(userId);
        if (history.size() > HISTORY_LIMIT) {
            for (int i = HISTORY_LIMIT; i < history.size(); i++) {
                passwordHistoryRepository.delete(history.get(i));
            }
        }
    }
}
