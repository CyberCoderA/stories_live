package com.heydrian.stories_live.services;

import org.springframework.stereotype.Service;

import com.heydrian.stories_live.models.users_models.Users;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.time.Instant;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

@Service
public class JWTService {
    @Value("${JWT_SECRET}")
    private String secretKey;

    @Value("${JWT_EXPIRATION_SECONDS:3600}")
    private long expirationSeconds;

    private SecretKey getKey() {
        byte[] secretBytes = secretKey == null
            ? new byte[0]
            : secretKey.getBytes(StandardCharsets.UTF_8);

        if (secretBytes.length < 32) {
            throw new IllegalStateException("JWT_SECRET must contain at least 32 characters");
        }

        return Keys.hmacShaKeyFor(secretBytes);
    }

    // Generates JWT token
    public String generateToken(Users user) {
        if (expirationSeconds <= 0) {
            throw new IllegalStateException("JWT_EXPIRATION_SECONDS must be greater than zero");
        }

        Instant now = Instant.now();

        return Jwts
            .builder()
            .subject(user.getUserEmail())
            .issuedAt(java.util.Date.from(now))
            .expiration(Date.from(now.plusSeconds(expirationSeconds)))
            .signWith(getKey())
            .compact();
    }

    public String extractEmail(String token) {
        Claims claims = extractAllClaims(token);

        Object emailClaim = claims.get("email");
        if (emailClaim instanceof String email && !email.isBlank()) {
            return email;
        }

        String subject = claims.getSubject();
        if (subject != null && !subject.isBlank()) {
            return subject;
        }

        return null;
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
            .verifyWith(getKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String email = extractEmail(token);
        return (email != null && email.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, claims -> claims.getExpiration());
    }
}