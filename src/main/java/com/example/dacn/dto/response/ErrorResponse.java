package com.example.dacn.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public class ErrorResponse{
    private String message;
    private HttpStatus statusCode;
}
