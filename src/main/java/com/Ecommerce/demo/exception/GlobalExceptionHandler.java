package com.Ecommerce.demo.exception;

import com.Ecommerce.demo.utils.ResponseUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MissingTokenException.class)
    public void handleMissingTokenException(MissingTokenException ex, HttpServletResponse response) throws IOException {
        ResponseUtils.sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(InvalidTokenException.class)
    public void handleInvalidTokenException(InvalidTokenException ex, HttpServletResponse response) throws IOException {
        ResponseUtils.sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public void handleGenericException(Exception ex, HttpServletResponse response) throws IOException {
        ResponseUtils.sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error - " + ex.getMessage());
    }
}
