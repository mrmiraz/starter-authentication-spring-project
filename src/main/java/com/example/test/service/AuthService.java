package com.example.test.service;

import com.example.test.domain.dto.*;
import com.example.test.domain.entity.RefreshToken;
import com.example.test.domain.entity.User;
import com.example.test.exception.ApiException;
import com.example.test.exception.BadRequestException;
import com.example.test.repository.RefreshTokenRepository;
import com.example.test.repository.UserRepository;
import com.example.test.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtils;
    private final RefreshTokenService refreshTokenService;

    public AuthService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtils, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.refreshTokenService = refreshTokenService;
    }

    public void registerUser(SignupRequest signupRequest) {
        if (signupRequest.getUsername() == null || signupRequest.getUsername().isEmpty() ||
                signupRequest.getPassword() == null || signupRequest.getPassword().isEmpty()) {
            throw new BadRequestException("Username and password must not be empty");
        }

        if (signupRequest.getType() == null) {
            throw new BadRequestException("User type must not be empty");
        }
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new ApiException("User already exists", "");
        }
        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        userRepository.save(user);
    }

    private void validateSignInRequest(SignInRequest request) {
        if (isBlank(request.getUsername()) || isBlank(request.getPassword())) {
            throw new BadRequestException("Username and password must not be empty.");
        }
        if (isBlank(request.getDeviceId()) || isBlank(request.getClientInstanceId())) {
            throw new BadRequestException("Device ID and client instance ID must not be empty.");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public TokenResponse authenticateUser(SignInRequest signInRequest) {
        validateSignInRequest(signInRequest);
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            signInRequest.getUsername(),
                            signInRequest.getPassword()
                    )
            );

            MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
            User user = userDetails.getUser();

            String accessToken = jwtUtils.generateAccessToken(user);
            String refreshToken = refreshTokenService.createRefreshToken(signInRequest, user);

            return new TokenResponse(accessToken, refreshToken);
        } catch (UsernameNotFoundException | BadCredentialsException ex) {
            throw new ApiException("Invalid credentials!", "Please try again with valid credentials.");
        }
    }

    public TokenResponse refreshAccessToken(TokenRefreshRequest tokenRefreshRequest) {
        String refreshTokenStr = tokenRefreshRequest.getRefreshToken();
        String clientInstanceId = tokenRefreshRequest.getClientInstanceId();

        RefreshToken refreshToken = refreshTokenRepository
                .findByTokenAndClientInstanceId(refreshTokenStr, clientInstanceId)
                .orElseThrow(() -> new ApiException("Refresh token not found", "Token might be invalid or expired."));

        refreshTokenService.verifyExpiration(refreshToken);
        User user = refreshToken.getUser();
        String accessToken = jwtUtils.generateAccessToken(user);
        return new TokenResponse(accessToken, refreshTokenStr);
    }

    public void signOut(SignOutRequest signOutRequest) {
        String refreshTokenStr = signOutRequest.getRefreshToken();
        String clientInstanceId = signOutRequest.getClientInstanceId();
        RefreshToken refreshToken = refreshTokenRepository
                .findByTokenAndClientInstanceId(refreshTokenStr, clientInstanceId)
                .orElseThrow(() -> new ApiException("Refresh token not found", "Token might be invalid or expired."));
        refreshTokenRepository.delete(refreshToken);
    }
}
