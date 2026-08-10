package com.hungnhan.school_management.security;

import com.hungnhan.school_management.entity.User;
import com.hungnhan.school_management.repository.InvalidatedTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final InvalidatedTokenRepository invalidatedTokenRepository;

    @Value("${jwt.signerKey}")
    private String signerKey;

    @Value("${jwt.validDuration}")
    private long validDuration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(signerKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + validDuration * 1000);

        // Lấy vai trò chính của User. 
        // Trong user_roles, một user có thể có nhiều role, nhưng theo nghiệp vụ thường có 1 vai trò chính
        String roleScope = user.getRoles().stream()
                .map(role -> role.getName())
                .reduce((r1, r2) -> r1 + " " + r2)
                .orElse("");

        return Jwts.builder()
                .subject(user.getUsername())
                .claim("scope", roleScope)
                .claim("userId", user.getId())
                .id(UUID.randomUUID().toString())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    public String getUsernameFromJWT(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    public Claims getClaimsFromJWT(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateToken(String authToken) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(authToken)
                    .getPayload();

            String tokenId = claims.getId();
            if (invalidatedTokenRepository.existsById(tokenId)) {
                log.error("Token has been invalidated (logged out)");
                return false;
            }

            return true;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }
}
