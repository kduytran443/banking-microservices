package com.kduytran.accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data @AllArgsConstructor
public class ErrorResponseDto {
    private String path;
    private String message;
    private HttpStatus errorCode;
    private LocalDateTime errorTime;
}
