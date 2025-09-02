package com.toni.virtualpet.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();

        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "testSecretKeyForJWTTokensInTestEnvironment");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 3600000);
    }

    @Test
    void generateToken_ReturnsValidToken() {
        String token = jwtUtils.generateToken("toni", "ROLE_USER");

        assertNotNull(token);
        assertTrue(token.length() > 20);
    }

    @Test
    void getUsernameFromToken_ReturnsCorrectUsername() {
        String token = jwtUtils.generateToken("toni", "ROLE_USER");

        String username = jwtUtils.getUsernameFromToken(token);
        assertEquals("toni", username);
    }

    @Test
    void getRoleFromToken_ReturnsCorrectRole() {
        String token = jwtUtils.generateToken("toni", "ROLE_USER");

        String role = jwtUtils.getRoleFromToken(token);
        assertEquals("ROLE_USER", role);
    }

    @Test
    void validateToken_WithValidToken_ReturnsTrue() {
        String token = jwtUtils.generateToken("toni", "ROLE_USER");

        assertTrue(jwtUtils.validateToken(token));
    }

    @Test
    void isTokenExpired_WithFreshToken_ReturnsFalse() {
        String token = jwtUtils.generateToken("toni", "ROLE_USER");

        assertFalse(jwtUtils.isTokenExpired(token));
    }

    @Test
    void getExpirationDateFromToken_ReturnsFutureDate() {
        String token = jwtUtils.generateToken("toni", "ROLE_USER");

        Date expiration = jwtUtils.getExpirationDateFromToken(token);
        assertTrue(expiration.after(new Date()));
    }
}