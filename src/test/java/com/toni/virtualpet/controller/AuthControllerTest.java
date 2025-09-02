package com.toni.virtualpet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toni.virtualpet.dto.request.LoginRequest;
import com.toni.virtualpet.dto.request.RegisterRequest;
import com.toni.virtualpet.model.user.User;
import com.toni.virtualpet.model.user.enums.Role;
import com.toni.virtualpet.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        User user = User.builder()
                .username("toni")
                .email("toni@example.com")
                .password(passwordEncoder.encode("password123"))
                .role(Role.ROLE_USER)
                .build();

        userRepository.save(user);
    }

    @Test
    void login_ValidCredentials_ReturnsJwtToken() throws Exception {
        LoginRequest loginRequest = new LoginRequest("toni", "password123");

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.username").value("toni"))
                .andExpect(jsonPath("$.data.token").exists());
    }

    @Test
    void register_ValidInput_ReturnsUserResponse() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest(
                "newuser@example.com",
                "newuser",
                "newpassword123",
                Role.ROLE_USER
        );

        mockMvc.perform(post("/api/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User registered successfully"))
                .andExpect(jsonPath("$.data.username").value("newuser"))
                .andExpect(jsonPath("$.data.email").value("newuser@example.com"))
                .andExpect(jsonPath("$.data.role").value("ROLE_USER"));
    }

    @Test
    @WithMockUser(username = "toni", roles = "USER")
    void verifyToken_WithAuthenticatedUser_ReturnsSuccess() throws Exception {
        mockMvc.perform(get("/api/auth/verify"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Token is valid"))
                .andExpect(jsonPath("$.data").value("Authentication verified"));
    }

    @Test
    void verifyToken_WithoutAuthentication_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/auth/verify"))
                .andExpect(status().isForbidden());
    }
}