package com.Ecommerce.demo.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtils {
    /**
     * 獲取當前認證用戶的用戶名
     *
     * @return 當前用戶名，如果未認證則返回 "Anonymous"
     */
    public static String getAuthenticatedUsername() {
        // 從 SecurityContextHolder 中獲取 Authentication 對象
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 確認是否有身份驗證信息
        if (authentication == null || !authentication.isAuthenticated()) {
            return "Anonymous"; // 如果未通過身份驗證，返回匿名用戶
        }

        // 獲取用戶名或 UserDetails
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else if (principal instanceof String) {
            return (String) principal;
        } else {
            return "Unknown"; // 無法解析用戶信息
        }
    }
}
