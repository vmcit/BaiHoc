package com.example.springboot.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT Utility - Xử lý tạo và xác thực JWT Token
 */
@Component
public class JwtTokenProvider {
    
    @Value("${jwt.secret}")
    private String jwtSecret;
    
    @Value("${jwt.expiration}")
    private long jwtExpiration; // Access token expiration time in milliseconds
    
    @Value("${jwt.refresh-expiration}")
    private long refreshTokenExpiration; // Refresh token expiration time in milliseconds
    
    /**
     * Tạo Access Token (có nhúng roles vào claims)
     */
    public String generateAccessToken(String username, Long userId, java.util.Set<String> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("roles", roles);  // VD: ["ROLE_ADMIN", "ROLE_USER"]
        return createToken(claims, username, jwtExpiration);
    }

    /** Backward-compatible overload (không có roles) */
    public String generateAccessToken(String username, Long userId) {
        return generateAccessToken(username, userId, java.util.Set.of());
    }
    
    /**
     * Tạo Refresh Token
     */
    public String generateRefreshToken(String username, Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("type", "refresh");
        return createToken(claims, username, refreshTokenExpiration);
    }
    
    /**
     * Tạo JWT Token
     */
    private String createToken(Map<String, Object> claims, String subject, long expirationTime) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);
        
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }
    
    /**
     * Lấy username từ JWT Token
     */
    public String getUsernameFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }
    
    /**
     * Lấy userId từ JWT Token
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return ((Number) claims.get("userId")).longValue();
    }

    /**
     * Lấy roles từ JWT Token
     */
    @SuppressWarnings("unchecked")
    public java.util.Set<String> getRolesFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        Object rolesObj = claims.get("roles");
        if (rolesObj instanceof java.util.Collection<?> col) {
            return new java.util.HashSet<>((java.util.Collection<String>) col);
        }
        return java.util.Set.of();
    }

    /** Kiểm tra token có role cụ thể không */
    public boolean hasRole(String token, String role) {
        return getRolesFromToken(token).contains(role);
    }
    
    /**
     * Lấy expiration time từ JWT Token
     */
    public Date getExpirationDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }
    
    /**
     * Kiểm tra JWT Token có hết hạn hay không
     */
    public boolean isTokenExpired(String token) {
        Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.before(new Date());
    }
    
    /**
     * Kiểm tra JWT Token có hợp lệ hay không
     */
    public boolean validateToken(String token) {
        try {
            SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            System.err.println("JWT Token validation failed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Lấy Claims từ JWT Token
     */
    private Claims getClaimsFromToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    
    /**
     * Lấy thời gian hết hạn của token (tính từ bây giờ)
     */
    public long getTokenExpirationTime(String token) {
        Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.getTime() - System.currentTimeMillis();
    }
}

