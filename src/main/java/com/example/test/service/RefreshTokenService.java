package com.example.test.service;

import com.example.test.domain.dto.SignInRequest;
import com.example.test.domain.entity.RefreshToken;
import com.example.test.domain.entity.User;
import com.example.test.exception.ApiException;
import com.example.test.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Value("${jwt.refreshTokenExpirationMs}")
    private long refreshTokenDurationMs;
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public String createRefreshToken(SignInRequest signInRequest, User user) {
        RefreshToken refreshToken = refreshTokenRepository
                .findByClientInstanceId(signInRequest.getClientInstanceId())
                .orElseGet(RefreshToken::new);

        refreshToken.setUser(user);
        refreshToken.setClientInstanceId(signInRequest.getClientInstanceId());
        refreshToken.setDeviceId(signInRequest.getDeviceId());
        refreshToken.setDeviceName(signInRequest.getDeviceName());
        refreshToken.setPlatform(signInRequest.getPlatform());
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        refreshTokenRepository.save(refreshToken);
        return refreshToken.getToken();
    }

    public void verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new ApiException("Refresh token expired! Please make a new sign-in request.", "");
        }
    }
}
