package com.example.test.exception;


import com.example.test.domain.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Map your custom exceptions to corresponding HTTP statuses
    private static final Map<Class<? extends ApiException>, HttpStatus> EXCEPTION_STATUS_MAP = Map.of(
            com.example.test.exception.BadRequestException.class, HttpStatus.ACCEPTED
//            com.example.exception.ResourceNotFoundException.class, HttpStatus.NOT_FOUND
            // Add more mappings here as needed
    );

    @ExceptionHandler(ApiException.class)
    public ApiResponse<Object> handleApiException(ApiException ex) {
        HttpStatus status = EXCEPTION_STATUS_MAP.getOrDefault(ex.getClass(), HttpStatus.BAD_REQUEST);
        return ApiResponse.error(ex.getMessage(), ex.getDescription(), status);
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<Object> handleAllExceptions(Exception ex) {
        // Log exception here if needed
        return ApiResponse.error("Internal Server Error: " + ex.getMessage(), "",  HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
