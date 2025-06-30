package com.example.test.exception;


import com.example.test.domain.dto.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Map<Class<? extends ApiException>, HttpStatus> EXCEPTION_STATUS_MAP = Map.of(
            com.example.test.exception.BadRequestException.class, HttpStatus.BAD_REQUEST
            // Add more mappings here as needed
    );

    @ExceptionHandler(ApiException.class)
    public ApiResponse<Object> handleApiException(ApiException ex) {
        HttpStatus status = EXCEPTION_STATUS_MAP.getOrDefault(ex.getClass(), HttpStatus.BAD_REQUEST);
        return ApiResponse.error(ex.getMessage(), ex.getDescription(), status);
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<Object> handleAllExceptions(Exception ex) {
        log.error("Exception occurred: {}", ex.getMessage());
        return ApiResponse.error("Something went wrong! Please try again later.", "",  HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
