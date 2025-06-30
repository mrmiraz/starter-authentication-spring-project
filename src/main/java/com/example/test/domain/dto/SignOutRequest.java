package com.example.test.domain.dto;

import lombok.Data;

@Data
public class SignOutRequest {
    private String refreshToken;
    private String accessToken;
    private String clientInstanceId;;
}
