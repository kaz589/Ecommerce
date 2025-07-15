package com.Ecommerce.demo.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import static com.Ecommerce.demo.utils.SecurityUtils.getAuthenticatedUsername;

@Slf4j
@Aspect
@Component
public class CrudOperationLogAspect {

    // 定義切入點，攔截所有包含 CRUD 操作的方法
    @Pointcut("execution(* com.Ecommerce.demo.service.*.*(..))")
    public void crudOperationPointcut() {}

    // 定義 Before 切面，記錄方法執行前的操作
    @Before("crudOperationPointcut() && (execution(* Create*(..)) || execution(* update*(..)) || execution(* delete*(..))))")
    public void logBeforeCrudOperation(JoinPoint joinPoint) {
        // 獲取當前用戶
        String username = getAuthenticatedUsername();

        // 獲取方法名稱
        String methodName = joinPoint.getSignature().getName();

        // 獲取方法參數
        Object[] args = joinPoint.getArgs();

        // 記錄日誌
        log.info("User '{}' is invoking method '{}' with arguments: {}", username, methodName, args);
    }

    // 定義 AfterReturning 切面，記錄方法執行後的返回結果
    @AfterReturning(pointcut = "crudOperationPointcut() && (execution(* Create*(..)) || execution(* update*(..)) || execution(* delete*(..)) )", returning = "result")
    public void logAfterCrudOperation(JoinPoint joinPoint, Object result) {
        // 獲取當前用戶
        String username = getAuthenticatedUsername();

        // 獲取方法名稱
        String methodName = joinPoint.getSignature().getName();

        // 記錄日誌
        log.info("User '{}' successfully invoked method '{}' with result: {}", username, methodName, result);
    }
}
