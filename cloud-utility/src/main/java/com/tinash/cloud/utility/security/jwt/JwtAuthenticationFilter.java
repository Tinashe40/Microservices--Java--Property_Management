package com.tinash.cloud.utility.security.jwt;

import com.tinash.cloud.utility.security.authentication.UserPrincipal;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Set<String> EXCLUDED_PATHS = Set.of(
            "/api/v1/auth/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/actuator/health",
            "/error"
    );

    private final JwtTokenProvider tokenProvider;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        if (shouldNotFilter(httpServletRequest)) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        String token = extractToken(httpServletRequest);

        if (StringUtils.hasText(token) && tokenProvider.validateToken(token)) {
            setAuthentication(token);
        } else {
            log.debug("No valid JWT token found for request: {}", httpServletRequest.getRequestURI());
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String extractToken(HttpServletRequest httpServletRequest) {
        String header = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(header) && header.startsWith("Bearer ")) {
            return header.substring(7);
        }

        return null;
    }

    private void setAuthentication(String token) {
        try {
            String username = tokenProvider.extractUsername(token);
            List<String> authorities = tokenProvider.extractAuthorities(token);

            var auth = new UsernamePasswordAuthenticationToken(
                    UserPrincipal.of(username, token),
                    token,
                    authorities.stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList()
            );

            SecurityContextHolder.getContext().setAuthentication(auth);
            log.debug("Authenticated user: {} with roles: {}", username, authorities);

        } catch (Exception e) {
            log.warn("Failed to set authentication from JWT token", e);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return EXCLUDED_PATHS.stream()
                .anyMatch(pattern -> pathMatcher.match(pattern, path));
    }
}