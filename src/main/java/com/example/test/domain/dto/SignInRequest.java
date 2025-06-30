package com.example.test.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignInRequest {
    private String username;
    private String password;

    // Device and client info for session tracking
    private String deviceId;          // physical device ID (optional but recommended)
    private String clientInstanceId;  // unique browser/app instance ID (required for per-session tokens)
    private String deviceName;        // "iPhone 15", "Chrome on Windows"
    private String platform;          // "iOS", "Windows", "Android"
}
