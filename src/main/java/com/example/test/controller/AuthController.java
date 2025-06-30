package com.example.test.controller;

import com.example.test.domain.dto.*;
import com.example.test.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthController  {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping("/signup")
    public ApiResponse<?> registerUser(@RequestBody SignupRequest signupRequest) {
        authService.registerUser(signupRequest);
        return ApiResponse.success("User registered successfully!", "", "", HttpStatus.OK);
    }

    @PostMapping("/signin")
    public ApiResponse<?> authenticateUser(@RequestBody SignInRequest signInRequest) {
        TokenResponse tokenResponse = authService.authenticateUser(signInRequest);
        return ApiResponse.success("Generated token successfully", "", tokenResponse, HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public ApiResponse<?> refreshToken(@RequestBody TokenRefreshRequest tokenRefreshRequest) {
        TokenResponse tokenResponse = authService.refreshAccessToken(tokenRefreshRequest);
        return ApiResponse.success("", "", tokenResponse, HttpStatus.OK);
    }

    @PostMapping("/sign-out")
    public ApiResponse<?> singOut(@RequestBody TokenRefreshRequest tokenRefreshRequest) {
        TokenResponse tokenResponse = authService.refreshAccessToken(tokenRefreshRequest);
        return ApiResponse.success("", "", tokenResponse, HttpStatus.OK);
    }
}
