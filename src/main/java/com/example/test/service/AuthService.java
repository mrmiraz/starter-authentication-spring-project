package com.example.test.service;

import com.example.test.domain.dto.SignInRequest;
import com.example.test.domain.dto.SignupRequest;
import com.example.test.domain.dto.TokenResponse;
import com.example.test.domain.entity.User;
import com.example.test.exception.ApiException;
import com.example.test.exception.BadRequestException;
import com.example.test.repository.UserRepository;
import com.example.test.util.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;

import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtils;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtil jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    public TokenResponse authenticateUser(SignInRequest signInRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            signInRequest.getUsername(),
                            signInRequest.getPassword()
                    )
            );

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtils.generateToken(userDetails.getUsername(), "MRZ");
            return new TokenResponse(token, "");

        } catch (UsernameNotFoundException | BadCredentialsException ex) {
            throw new ApiException("Invalid username or password", "Please try again!");
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
