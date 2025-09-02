package com.toni.virtualpet.security;

import com.toni.virtualpet.model.user.User;
import com.toni.virtualpet.model.user.enums.Role;
import com.toni.virtualpet.repository.UserRepository;
import com.toni.virtualpet.service.user.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDetailsServiceImplTest {

    private UserRepository userRepository;
    private UserDetailsServiceImpl service;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        service = new UserDetailsServiceImpl(userRepository);
    }

    @Test
    void loadUserByUsername_UserExists_ReturnsUserPrincipal() {
        User user = User.builder()
                .id(1L)
                .username("toni")
                .email("toni@example.com")
                .password("encodedPass")
                .role(Role.ROLE_USER)
                .build();

        when(userRepository.findByUsername("toni")).thenReturn(Optional.of(user));

        UserPrincipal principal = (UserPrincipal) service.loadUserByUsername("toni");

        assertEquals("toni", principal.getUsername());
        assertEquals("encodedPass", principal.getPassword());
        assertEquals(1L, principal.getId());
        assertTrue(principal.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void loadUserByUsername_UserNotFound_ThrowsException() {
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("ghost"));
    }
}