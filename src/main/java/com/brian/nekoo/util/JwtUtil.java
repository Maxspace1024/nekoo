package com.brian.nekoo.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    private final String secret;
    private final Key secretKey;
    private final long expirationTime = 3600000;

    public JwtUtil(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    // 生成 JWT
    public String generateToken(String username, Map<String, Object> claims) {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
            .signWith(secretKey)
            .compact();
    }

    // 驗證 JWT
    public boolean validateToken(String token, String username) {
        String extractedUsername = extractUsername(token);
        return extractedUsername.equals(username) && !isTokenExpired(token);
    }

    // 從 JWT 中提取用戶名
    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    // 檢查 Token 是否過期
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // 提取 Token 的到期時間
    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

    // 提取所有 Claims
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public String resolveToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

