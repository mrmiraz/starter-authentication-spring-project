package com.example.test.controller;

import com.example.test.domain.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class AuthTestController {

    @GetMapping("/all")
    public ApiResponse<?> allAccess() {
        return ApiResponse.success("Access", "Do not need any access token", "", HttpStatus.OK);
    }

    @GetMapping("/user")
    public ApiResponse<?> userAccess() {
        return ApiResponse.success("Access", "Congratulations, you successfully access with your token!", "", HttpStatus.OK);
    }
}
