package com.kduytran.accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class ResponseDto {
    private String status;
    private String message;
}
