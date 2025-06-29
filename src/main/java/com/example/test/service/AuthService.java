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

import java.util.Optional;

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

    public TokenResponse refreshTokenResponse(TokenRefreshRequest tokenRefreshRequest) {
        try {
            String token = tokenRefreshRequest.getToken();

            Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByToken(token);
            if (optionalRefreshToken.isEmpty()) {
                throw new ApiException("Refresh token not found", "Token might be invalid or expired.");
            }

            RefreshToken refreshToken = refreshTokenService.verifyExpiration(optionalRefreshToken.get());
            User user = refreshToken.getUser();

            String accessToken = jwtUtils.generateAccessToken(user);

            TokenResponse tokenResponse = new TokenResponse(accessToken, token);
            return tokenResponse;
        } catch (RuntimeException ex) {
            throw new ApiException("Refresh token not found", "Please make a new sign-in request.");
        }
    }

    public TokenResponse authenticateUser(SignInRequest signInRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            signInRequest.getUsername(),
                            signInRequest.getPassword()
                    )
            );

            MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
            String accessToken = jwtUtils.generateAccessToken(userDetails.getUser());
            String refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());
            return new TokenResponse(accessToken, refreshToken);
        } catch (UsernameNotFoundException ex) {
            throw new ApiException("Invalid username!", "Please try again with valid credential!");
        } catch (BadCredentialsException ex) {
            throw new ApiException("Invalid password!", "Please try again with valid credential!");
        }
    }

    public void registerUser(SignupRequest signupRequest) {
        if (signupRequest.getUsername() == null || signupRequest.getUsername().isEmpty() ||
                signupRequest.getPassword() == null || signupRequest.getPassword().isEmpty()) {
            throw new BadRequestException("Username and password must not be empty");
        }

        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new ApiException("User already exists", "");
        }
        User user = new User(null, signupRequest.getUsername(), passwordEncoder.encode(signupRequest.getPassword()));
        userRepository.save(user);
    }
}
