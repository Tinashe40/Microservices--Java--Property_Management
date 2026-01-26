package com.tinash.cloud.utility.audit;

import com.tinash.cloud.utility.security.util.SecurityUtils;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Implementation of AuditorAware to provide current user for auditing.
 */
@Component
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        try {
            return SecurityUtils.getCurrentUser();
        } catch (Exception e) {
            return Optional.of("SYSTEM");
        }
    }
}