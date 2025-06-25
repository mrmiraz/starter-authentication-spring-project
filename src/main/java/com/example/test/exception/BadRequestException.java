package com.example.test.exception;

public class BadRequestException extends ApiException{
    public BadRequestException(String description) {
            super("Bad Request.", description);
    }
}
