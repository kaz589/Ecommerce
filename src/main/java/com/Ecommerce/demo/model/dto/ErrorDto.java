package com.Ecommerce.demo.model.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class ErrorDto {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}
