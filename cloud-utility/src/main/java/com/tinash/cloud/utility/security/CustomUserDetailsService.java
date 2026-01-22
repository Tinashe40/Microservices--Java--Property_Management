package com.tinash.cloud.utility.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * An interface that extends Spring Security's UserDetailsService.
 * Consuming microservices should implement this interface to provide their
 * specific UserDetails loading logic, typically by fetching user information
 * from a database or another service.
 */
public interface CustomUserDetailsService extends UserDetailsService {

    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may be case-sensitive or case-insensitive depending on how the
     * implementation instance is configured.
     *
     * @param username the username identifying the user whose data is required.
     * @return a fully populated user record (never null)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     *                                   GrantedAuthority
     */
    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
