package com.Ecommerce.demo.config;

import com.Ecommerce.demo.filter.JwtAuthenticationFilter;
import com.Ecommerce.demo.service.SecurityUserService;
import com.Ecommerce.demo.utils.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {
    @Autowired
    private final JwtTokenProvider jwtTokenProvider;

    public SecurityConfig(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * 配置 Spring Security 的核心安全邏輯，包括 CORS、CSRF、授權規則及自定義的過濾器
     *
     * @param http HttpSecurity 對象，用於設置安全性配置
     * @return SecurityFilterChain 安全過濾鏈
     * @throws Exception 配置可能拋出的異常
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 配置 CORS（跨來源請求）規則，使用自定義的 corsConfigurationSource 方法
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // 使用自定義的 CORS 配置
                // 禁用 CSRF（跨站請求偽造）保護，通常在 REST API 中禁用
                .csrf(csrf -> csrf.disable())
                // 設置授權規則
                .authorizeHttpRequests(auth -> auth
                                // 允許匿名訪問的路徑，例如登入 API 和 Swagger UI
                                .requestMatchers("/api/login", "/swagger-ui/**").permitAll() // 允許登入路徑
                                // 其他所有請求均需要授權
                                .anyRequest().authenticated() // 其他請求需要登入
                        // .requestMatchers("/login","/swagger-ui/**","/api/users/**").permitAll() // 允許登入路徑

                )
                // 在 UsernamePasswordAuthenticationFilter 之前添加自定義的 JWT 驗證過濾器
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    /**
     * 配置 CORS（跨來源請求）的規則，允許特定來源和方法
     * @return CorsConfigurationSource CORS 配置源
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true); // 如果你前端有帶 cookie 或 auth header 的話
        // 設置 CORS 配置的作用範圍（所有路徑）
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
    // 注入自定義的 SecurityUserService，用於處理使用者認證邏輯
    @Autowired
    private SecurityUserService securityUserService;

    /**
     * 配置 AuthenticationManager，用於管理認證提供者
     * @param http HttpSecurity 對象
     * @param authConfig AuthenticationConfiguration 用於獲取預設的認證配置
     * @return AuthenticationManager 認證管理器
     * @throws Exception 配置可能拋出的異常
     */
    @Bean
    public AuthenticationManager authManager(HttpSecurity http, AuthenticationConfiguration authConfig) throws Exception {
        // 使用 ProviderManager 管理 DaoAuthenticationProvider
        return new ProviderManager(List.of(daoAuthenticationProvider()));
    }

    /**
     * 配置 DaoAuthenticationProvider，用於處理帳號密碼的認證邏輯
     * @return DaoAuthenticationProvider 帳號密碼認證提供者
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider
                = new DaoAuthenticationProvider();
        // 設置自定義的 UserDetailsService（從資料庫加載使用者資料）
        authenticationProvider.setUserDetailsService(securityUserService);
        // 設置密碼加密器（使用 BCryptPasswordEncoder）
        authenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return authenticationProvider;
    }

    /**
     * 配置密碼加密器，用於加密和驗證密碼
     * @return PasswordEncoder 密碼加密器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

