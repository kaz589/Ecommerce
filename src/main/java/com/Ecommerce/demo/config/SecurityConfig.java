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
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // 使用自定義的 CORS 配置
                .csrf(csrf -> csrf.disable()) // 使用新的配置方式禁用 CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login","/swagger-ui/**").permitAll() // 允許登入路徑
                        .anyRequest().authenticated() // 封鎖其他所有請求
                       // .requestMatchers("/login","/swagger-ui/**","/api/users/**").permitAll() // 允許登入路徑

                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true); // 如果你前端有帶 cookie 或 auth header 的話

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
    @Autowired
    private SecurityUserService securityUserService;
    //使用 AuthenticationManager 驗證 email + password
    @Bean
    public AuthenticationManager authManager(HttpSecurity http, AuthenticationConfiguration authConfig) throws Exception {
        return new ProviderManager(List.of(daoAuthenticationProvider()));
    }

    //用來產出 Spring Security 預設的密碼驗證器： (daoAuthenticationProvider()	驗證「帳號 + 密碼」的邏輯封裝器)
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider
                = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(securityUserService); 		// 你自己的 UserDetailsService
        authenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());		// 通常是 BCryptPasswordEncoder
        return authenticationProvider;
    }

    //加密
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

