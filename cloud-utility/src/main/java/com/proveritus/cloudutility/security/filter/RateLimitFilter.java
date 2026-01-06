package com.proveritus.cloudutility.security.filter;

import com.proveritus.cloudutility.security.ratelimit.RateLimitService;
import com.proveritus.cloudutility.security.ratelimit.RateLimitConfiguration;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter for rate limiting HTTP requests.
 */
@Component 
@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {

    private final RateLimitService rateLimitService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        String key = resolveRateLimitKey(request);
        
        boolean allowed = rateLimitService.allowRequest(
                key,
                RateLimitConfiguration.globalLimitBucketConfiguration()
        );

        if (!allowed) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Rate limit exceeded. Please try again later.");
            return;
        }

        // Add rate limit headers
        long remaining = rateLimitService.getRemainingTokens(
                key,
                RateLimitConfiguration.globalLimitBucketConfiguration()
        );
        response.addHeader("X-Rate-Limit-Remaining", String.valueOf(remaining));

        filterChain.doFilter(request, response);
    }

    private String resolveRateLimitKey(HttpServletRequest request) {
        // Try to get user ID from authentication, fallback to IP
        String userId = request.getUserPrincipal() != null 
                ? request.getUserPrincipal().getName() 
                : null;
        
        if (userId != null) {
            return "user:" + userId;
        }
        
        return "ip:" + getClientIP(request);
    }

    private String getClientIP(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}