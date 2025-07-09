package com.Ecommerce.demo.utils;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
@Component
public class JwtTokenProvider {
    private final String JWT_SECRET = "your_very_long_secret_key_that_is_at_least_256_bits_and_can_be_extended_to_512_bits_for_stronger_security_use_in_production_environment";  // 确保这个密钥安全且满足512位

    private final long JWT_EXPIRATION = 86400000L; // 1 天

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(JWT_SECRET.getBytes());
    }


    // 生成 JWT Token
    public String generateToken(String username, List<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))
                .signWith(getSignKey(), SignatureAlgorithm.HS512)
                .compact();
    }
    // 新增：支援只傳單一角色（自動轉成 List）
    public String generateToken(String username, String role) {
        if (role == null) {
            return generateToken(username, List.of("USER")); // 預設給 USER 角色
        }
        return generateToken(username, List.of(role));
    }


    // 從 JWT Token 中提取用戶名
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 驗證 JWT Token 是否有效
    public boolean validateToken(String token) {
        try {
            getUsernameFromToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
