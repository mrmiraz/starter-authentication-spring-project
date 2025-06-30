package com.example.test.domain.dto;

import lombok.Data;

@Data
public class TokenRefreshRequest {
    private String refreshToken;
    private String clientInstanceId;
    private String oldAccessToken;
}
