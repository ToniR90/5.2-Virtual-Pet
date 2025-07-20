package com.toni.virtualpel.controller;

import com.toni.virtualpel.dto.request.LoginRequest;
import com.toni.virtualpel.dto.request.RegisterRequest;
import com.toni.virtualpel.dto.response.ApiResponse;
import com.toni.virtualpel.dto.response.JwtResponse;
import com.toni.virtualpel.dto.response.UserResponse;
import com.toni.virtualpel.model.User;
import com.toni.virtualpel.service.AuthService;
import jakarta.validation.Valid;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            logger.info("Login request for user: {}", loginRequest.getUsername());

            JwtResponse jwtResponse = authService.login(loginRequest);

            return ResponseEntity.ok(new ApiResponse<>(true, "Login exitoso", jwtResponse));
        } catch (Exception e) {
            logger.error("Login error for user {}: {}", loginRequest.getUsername(), e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, "Invalid credentials"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            logger.info("Register request for user: {} with email: {}",
                    registerRequest.getUsername(), registerRequest.getEmail());

            User user = authService.register(registerRequest);

            return ResponseEntity.ok(new ApiResponse<>(true, "Register done",
                    new UserResponse(user.getId(), user.getEmail(), user.getUserName(), user.getRole().name())));
        } catch (Exception e) {
            logger.error("Register error for user {}: {}", registerRequest.getUsername(), e.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(false, e.getMessage()));
        }
    }
}
