package com.heydrian.stories_live.services;

import org.springframework.stereotype.Service;

import com.heydrian.stories_live.models.users_models.Users;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.time.Instant;
import java.util.Date;
import java.util.Base64;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

@Service
public class JWTService {
    @Value("${jwt.secret:medicore-super-secret-key-that-is-at-least-32-bytes-long}")
    private String secretKey;

    private SecretKey getKey() {
        String normalizedKey = secretKey;
        if (normalizedKey.length() < 32) {
            normalizedKey = String.format("%-32s", normalizedKey).replace(' ', '0');
        }
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(Base64.getEncoder().encodeToString(normalizedKey.getBytes())));
    }

    // Generates JWT token
    public String generateToken(Users user) {
        Instant now = Instant.now();

        return Jwts
            .builder()
            .subject(user.getUserEmail())
            .issuedAt(java.util.Date.from(now))
            .expiration(java.util.Date.from(now.plusSeconds(60 * 60))) // Token valid for 1 hour
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