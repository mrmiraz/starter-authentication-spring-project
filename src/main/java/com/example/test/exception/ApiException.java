package com.example.test.exception;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private final String description;

    public ApiException(String message, String description) {
        super(message);
        this.description = description;
    }
}
