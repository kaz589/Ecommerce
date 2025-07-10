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
    /**
     * JWT 的密鑰，用於簽名和驗證 Token。
     * 注意：密鑰必須足夠長且安全，建議在生產環境中使用環境變數存儲。
     */
    private final String JWT_SECRET = "your_very_long_secret_key_that_is_at_least_256_bits_and_can_be_extended_to_512_bits_for_stronger_security_use_in_production_environment";  // 确保这个密钥安全且满足512位
    /**
     * Token 的有效期（以毫秒計算）。
     * 這裡設置為 1 天（86400000 毫秒）。
     */
    private final long JWT_EXPIRATION = 86400000L; // 1 天

    /**
     * 獲取簽名密鑰，使用 HMAC-SHA 演算法。
     * @return Key 用於 JWT 的簽名。
     */
    private Key getSignKey() {
        return Keys.hmacShaKeyFor(JWT_SECRET.getBytes());
    }


    /**
     * 生成 JWT Token，包含使用者名稱和角色。
     * @param username 使用者名稱。
     * @param roles 使用者角色列表。
     * @return 生成的 JWT Token。
     */
    public String generateToken(String username, List<String> roles) {
        return Jwts.builder()
                .setSubject(username)// 設置使用者名稱作為主體（Subject）
                .claim("roles", roles)// 添加使用者角色到 Token 的 Payload
                .setIssuedAt(new Date())// 設置簽發時間
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION))// 設置過期時間
                .signWith(getSignKey(), SignatureAlgorithm.HS512)// 使用 HS512 演算法進行簽名
                .compact();
    }

    /**
     * 生成 JWT Token，支援單一角色（自動轉換為角色列表）。
     * @param username 使用者名稱。
     * @param role 單一角色（如果為 null，則默認為 "USER"）。
     * @return 生成的 JWT Token。
     */
    public String generateToken(String username, String role) {
        if (role == null) {
            return generateToken(username, List.of("USER")); // 預設給 USER 角色
        }
        return generateToken(username, List.of(role));
    }


    /**
     * 從 JWT Token 中解析出使用者名稱（Subject）。
     * @param token JWT Token。
     * @return 使用者名稱。
     * @throws JwtException 當 Token 無效或解析失敗時拋出。
     */
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * 驗證 JWT Token 是否有效。
     * @param token JWT Token。
     * @return 如果 Token 有效，返回 true；否則返回 false。
     */
    public boolean validateToken(String token) {
        try {
            getUsernameFromToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
