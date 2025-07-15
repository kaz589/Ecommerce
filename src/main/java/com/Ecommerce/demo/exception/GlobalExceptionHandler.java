package com.Ecommerce.demo.exception;

import com.Ecommerce.demo.utils.ResponseUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;

import static com.Ecommerce.demo.utils.SecurityUtils.getAuthenticatedUsername;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BusinessException.class)
    public void handleBusinessException(BusinessException ex, HttpServletResponse response) throws IOException, IOException {
        // 記錄簡單的業務錯誤描述
        logger.warn("Business exception occurred: User {},{}",  getAuthenticatedUsername(),ex.getMessage());


        // 使用 ResponseUtils 返回錯誤訊息
        System.out.println(ex.getMessage());
        ResponseUtils.sendErrorResponse(response, HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    @ExceptionHandler(SystemException.class)
    public void handleSystemException(SystemException ex, HttpServletResponse response) throws IOException {
        logger.error("System exception occurred: {}", ex.getMessage());
        // 不返回具體的錯誤信息，僅設置狀態碼
        ResponseUtils.sendErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), "系統錯誤，請稍後再試");
    }

    @ExceptionHandler(Exception.class)
    public void handleOtherExceptions(Exception ex, HttpServletResponse response) throws IOException {
        // 捕捉其他未處理的例外，返回通用錯誤訊息
        logger.error("Unhandled exception occurred", ex);
        ResponseUtils.sendErrorResponse(response, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error");
    }
}
