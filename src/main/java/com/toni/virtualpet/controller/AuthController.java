package com.toni.virtualpet.controller;

import com.toni.virtualpet.dto.request.LoginRequest;
import com.toni.virtualpet.dto.request.RegisterRequest;
import com.toni.virtualpet.dto.response.ApiResponse;
import com.toni.virtualpet.dto.response.JwtResponse;
import com.toni.virtualpet.dto.response.UserResponse;
import com.toni.virtualpet.model.user.User;
import com.toni.virtualpet.service.user.AuthService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        logger.info("Login request for user: {}", loginRequest.getUsername());

        JwtResponse jwtResponse = authService.login(loginRequest);

        logger.info("Login successful for user: {}", loginRequest.getUsername());
        return ResponseEntity.ok(
                ApiResponse.success("Login successful", jwtResponse)
        );
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        logger.info("Registration request for user: {} with email: {}",
                registerRequest.getUsername(), registerRequest.getEmail());

        User user = authService.register(registerRequest);
        UserResponse userResponse = UserResponse.from(user);

        logger.info("Registration successful for user: {}", user.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("User registered successfully", userResponse));
    }

    @GetMapping("/verify")
    public ResponseEntity<ApiResponse<String>> verifyToken() {
        return ResponseEntity.ok(
                ApiResponse.success("Token is valid", "Authentication verified")
        );
    }
}