package com.example.test.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private String status; //success/error
    private int code;
    private String message;
    private String description;
    private T data; 
    private Long timestamp;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .status("success")
                .code(HttpStatus.OK.value())
                .message("")
                .description("")
                .data(data)
                .timestamp(System.currentTimeMillis())
                .build();
    }
    
    public static <T> ApiResponse<T> success(String message, T data, HttpStatus status) {
        return ApiResponse.<T>builder()
                .status("success")
                .code(status.value())
                .message(message)
                .description("")
                .data(data)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    public static <T> ApiResponse<T> error(String message, String description, HttpStatus status) {
        return ApiResponse.<T>builder()
                .status("error")
                .message(message)
                .description(Optional.ofNullable(description).orElse(""))
                .code(status.value())
                .timestamp(System.currentTimeMillis())
                .build();
    }
}

