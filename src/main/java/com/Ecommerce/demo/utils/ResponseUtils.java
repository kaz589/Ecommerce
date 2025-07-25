package com.Ecommerce.demo.utils;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ResponseUtils {
    public static void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setCharacterEncoding("UTF-8"); // 設置字符編碼
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write("{\"status\":" + status + ",\"message\":\"" + message + "\"}");
    }
}
