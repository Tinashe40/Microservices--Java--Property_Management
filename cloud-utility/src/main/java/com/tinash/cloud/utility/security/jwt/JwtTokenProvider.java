package com.tinash.cloud.utility.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;
    private final JwtTokenBlacklist tokenBlacklist;
    private SecretKey cachedKey;

    private SecretKey getSigningKey() {
        if (cachedKey == null) {
            cachedKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
        }
        return cachedKey;
    }

    public String generateToken(Authentication authentication) {
        return generateToken(authentication.getName(), extractAuthorities(authentication));
    }

    public String generateToken(String username, List<String> authorities) {
        Instant now = Instant.now();
        Instant expiry = now.plus(jwtProperties.getExpiration(), ChronoUnit.MILLIS);

        return Jwts.builder()
                .subject(username)
                .claim("auth", String.join(",", authorities))
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(getSigningKey())
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String token) {
        if (tokenBlacklist.isBlacklisted(token)) {
            log.debug("Token is blacklisted");
            return false;
        }

        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (SignatureException ex) {
            log.debug("Invalid JWT signature: {}", ex.getMessage());
        } catch (MalformedJwtException ex) {
            log.debug("Invalid JWT token: {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            log.debug("Expired JWT token: {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            log.debug("Unsupported JWT token: {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            log.debug("JWT claims string is empty: {}", ex.getMessage());
        }
        return false;
    }

    public void invalidateToken(String token) {
        tokenBlacklist.blacklist(token);
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public List<String> extractAuthorities(String token) {
        String authorities = extractClaims(token).get("auth", String.class);
        return StringUtils.hasText(authorities)
                ? List.of(authorities.split(","))
                : List.of();
    }

    private List<String> extractAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }
}