package com.tinash.cloud.utility.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenValidator {

    private final JwtProperties jwtProperties;

    public JwtTokenValidator(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public boolean validate(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(jwtProperties.getSecret().getBytes()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(jwtProperties.getSecret().getBytes()).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
