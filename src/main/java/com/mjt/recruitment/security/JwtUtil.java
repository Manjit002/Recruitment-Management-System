package com.mjt.recruitment.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${app.jwtSecret}")
    private String jwtSecretRaw;

    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    private SecretKey signingKey;

    @PostConstruct
    public void init() {
        String s = jwtSecretRaw == null ? "" : jwtSecretRaw.trim();
        byte[] keyBytes = s.getBytes(StandardCharsets.UTF_8);

        // Ensure at least 32 bytes (256 bits) for HS256 by repeating/padding
        if (keyBytes.length < 32) {
            byte[] padded = new byte[32];
            for (int i = 0; i < padded.length; i++) {
                padded[i] = keyBytes.length == 0 ? (byte) 0 : keyBytes[i % keyBytes.length];
            }
            keyBytes = padded;
        }

        signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token).getBody();
        return claims.get("role", String.class);
    }
}
