package com.toni.virtualpet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toni.virtualpet.dto.request.LoginRequest;
import com.toni.virtualpet.dto.request.RegisterRequest;
import com.toni.virtualpet.dto.response.JwtResponse;
import com.toni.virtualpet.dto.response.UserResponse;
import com.toni.virtualpet.model.User;
import com.toni.virtualpet.model.enums.Role;
import com.toni.virtualpet.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void login_ValidCredentials_ReturnsJwtToken() throws Exception {
        LoginRequest loginRequest = new LoginRequest("testuser", "password");
        JwtResponse jwtResponse = JwtResponse.builder()
                .token("fake-jwt-token")
                .type("Bearer")
                .id(1L)
                .username("testuser")
                .role(Role.ROLE_USER)
                .build();

        when(authService.login(any(LoginRequest.class))).thenReturn(jwtResponse);

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Login successful"))
                .andExpect(jsonPath("$.data.token").value("fake-jwt-token"))
                .andExpect(jsonPath("$.data.username").value("testuser"))
                .andExpect(jsonPath("$.data.role").value("ROLE_USER"));
    }

    @Test
    void login_InvalidInput_ReturnsBadRequest() throws Exception {
        LoginRequest loginRequest = new LoginRequest("", "password");

        mockMvc.perform(post("/api/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_ValidInput_ReturnsUserResponse() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest("test@example.com", "testuser", "password123");

        User user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .role(Role.ROLE_USER)
                .build();

        UserResponse userResponse = UserResponse.from(user);

        when(authService.register(any(RegisterRequest.class))).thenReturn(user);

        mockMvc.perform(post("/api/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User registered successfully"))
                .andExpect(jsonPath("$.data.username").value("testuser"))
                .andExpect(jsonPath("$.data.email").value("test@example.com"))
                .andExpect(jsonPath("$.data.role").value("ROLE_USER"));
    }

    @Test
    void register_InvalidEmail_ReturnsBadRequest() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest("invalid-email", "testuser", "password123");

        mockMvc.perform(post("/api/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_ShortPassword_ReturnsBadRequest() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest("test@example.com", "testuser", "123");

        mockMvc.perform(post("/api/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void register_ShortUsername_ReturnsBadRequest() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest("test@example.com", "ab", "password123");

        mockMvc.perform(post("/api/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void verifyToken_ValidToken_ReturnsSuccess() throws Exception {
        mockMvc.perform(get("/api/auth/verify"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Token is valid"))
                .andExpect(jsonPath("$.data").value("Authentication verified"));
    }

    @Test
    void verifyToken_NoToken_ReturnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/auth/verify"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loginEndpoint_PublicAccess_Allowed() throws Exception {
        LoginRequest loginRequest = new LoginRequest("testuser", "password");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void registerEndpoint_PublicAccess_Allowed() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest("test@example.com", "testuser", "password123");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated());
    }
}