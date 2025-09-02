package com.toni.virtualpet.service.user;

import com.toni.virtualpet.dto.request.LoginRequest;
import com.toni.virtualpet.dto.request.RegisterRequest;
import com.toni.virtualpet.dto.response.JwtResponse;
import com.toni.virtualpet.exception.personalizedException.UnauthorizedException;
import com.toni.virtualpet.exception.personalizedException.UserAlreadyExistsException;
import com.toni.virtualpet.model.user.User;
import com.toni.virtualpet.model.user.enums.Role;
import com.toni.virtualpet.repository.UserRepository;
import com.toni.virtualpet.security.JwtUtils;
import com.toni.virtualpet.security.UserPrincipal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;

    private AuthService authService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        authenticationManager = mock(AuthenticationManager.class);
        jwtUtils = mock(JwtUtils.class);

        authService = new AuthService(userRepository, passwordEncoder, authenticationManager, jwtUtils);
    }

    @Test
    void login_WithValidCredentials_ReturnsJwtResponse() {
        LoginRequest request = new LoginRequest("toni", "password123");

        UserPrincipal principal = new UserPrincipal(1L, "toni", "toni@example.com",
                "encodedPass", List.of(new SimpleGrantedAuthority("ROLE_USER")));
        Authentication authentication = mock(Authentication.class);

        when(authentication.getPrincipal()).thenReturn(principal);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtUtils.generateToken("toni", "ROLE_USER")).thenReturn("fake-token");

        JwtResponse response = authService.login(request);

        assertEquals("toni", response.getUsername());
        assertEquals("fake-token", response.getToken());
        assertEquals(Role.ROLE_USER, response.getRole());
    }

    @Test
    void register_WithValidData_SavesUserAndReturnsIt() {
        RegisterRequest request = new RegisterRequest("new@example.com", "newuser", "newpass", Role.ROLE_USER);

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(passwordEncoder.encode("newpass")).thenReturn("encodedPass");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        when(userRepository.save(userCaptor.capture())).thenAnswer(inv -> inv.getArgument(0));

        User savedUser = authService.register(request);

        assertEquals("newuser", savedUser.getUsername());
        assertEquals("new@example.com", savedUser.getEmail());
        assertEquals("encodedPass", savedUser.getPassword());
        assertEquals(Role.ROLE_USER, savedUser.getRole());
    }

    @Test
    void register_WithExistingUsername_ThrowsException() {
        RegisterRequest request = new RegisterRequest("email@example.com", "toni", "pass", Role.ROLE_USER);
        when(userRepository.existsByUsername("toni")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> authService.register(request));
    }

    @Test
    void register_WithExistingEmail_ThrowsException() {
        RegisterRequest request = new RegisterRequest("toni@example.com", "newuser", "pass", Role.ROLE_USER);
        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("toni@example.com")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> authService.register(request));
    }

    @Test
    void register_WithAdminRole_ThrowsUnauthorizedException() {
        RegisterRequest request = new RegisterRequest("admin@example.com", "admin", "pass", Role.ROLE_ADMIN);
        when(userRepository.existsByUsername("admin")).thenReturn(false);
        when(userRepository.existsByEmail("admin@example.com")).thenReturn(false);

        assertThrows(UnauthorizedException.class, () -> authService.register(request));
    }
}