package com.Ecommerce.demo.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.Ecommerce.demo.utils.SecurityUtils.getAuthenticatedUsername;

@Slf4j
@Aspect
@Component
public class LoginLogAspect {

    private final HttpServletRequest request;

    // 使用建構子注入 HttpServletRequest
    public LoginLogAspect(HttpServletRequest request) {
        this.request = request;
    }
    @Pointcut("execution(* com.Ecommerce.demo.controller.AuthController.login(..))")
    public void  LoginLogAspect(){


    }
    @After("LoginLogAspect()")
    public void logLoginDetails(JoinPoint joinPoint) {

        //test
        // 獲取用戶名
        String username = getAuthenticatedUsername();
        // 登入時間
        LocalDateTime loginTime = LocalDateTime.now();
        // 定義時間格式，例如 "yyyy-MM-dd HH:mm:ss"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 格式化時間
        String formattedLoginTime = loginTime.format(formatter);
        //test2
        //  IP 地址
        String ipAddress = request.getRemoteAddr();

        //Test3
        // 記錄登入資訊
        log.info("login User:{},login time：{},login IP：{}", username,formattedLoginTime,ipAddress);

    }

}
