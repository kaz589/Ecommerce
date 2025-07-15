package com.Ecommerce.demo.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.Ecommerce.demo.utils.SecurityUtils.getAuthenticatedUsername;

@Aspect
@Component
@Slf4j
public class ProductChangeLogAspect {

    @Before("execution(* com.Ecommerce.demo.controller.ProductController.purchase(..))")
    public void logPurchaseOperation(JoinPoint joinPoint) {
        // 獲取方法參數
        Object[] args = joinPoint.getArgs();

        // 確保參數不為空，並且格式正確
        if (args.length > 0 && args[0] instanceof Map) {
            @SuppressWarnings("unchecked")
            Map<String, Object> data = (Map<String, Object>) args[0];

            // 從參數中提取商品 ID 和數量
            int productId = (Integer) data.get("productId");
            int quantity = (Integer) data.get("quantity");

            // 獲取用戶名
            String username = getAuthenticatedUsername();

            // 記錄日誌
            log.info("User '{}' purchased product with ID '{}' and quantity '{}'", username, productId, quantity);
        } else {
            log.warn("Invalid arguments passed to purchase method.");
        }
    }



}
