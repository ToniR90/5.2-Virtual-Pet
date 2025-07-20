package com.toni.virtualpel.service;

import com.toni.virtualpel.dto.response.JwtResponse;
import com.toni.virtualpel.dto.request.LoginRequest;
import com.toni.virtualpel.dto.request.RegisterRequest;
import com.toni.virtualpel.exception.UserAlreadyExistsException;
import com.toni.virtualpel.exception.UserNotFoundException;
import com.toni.virtualpel.model.User;
import com.toni.virtualpel.model.enums.Role;
import com.toni.virtualpel.repository.UserRepository;
import com.toni.virtualpel.security.JwtUtils;
import com.toni.virtualpel.security.UserPrincipal;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    public JwtResponse login(LoginRequest loginRequest) {
        logger.info("Login try for user: {}" , loginRequest.getUsername());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername() , loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        User user = userRepository.findByUsername(userPrincipal.getUsername())
                .orElseThrow(() -> new UserNotFoundException("Can't find the user"));

        String token = jwtUtils.generateToken(userPrincipal.getUsername(), user.getRole().name());

        logger.info("Login done: {}", loginRequest.getUsername());

        return JwtResponse.builder()
                .token(token)
                .id(user.getId())
                .username(user.getUsername())
                .role(user.getRole())
                .build();
    }

    public User register(RegisterRequest registerRequest) {
        logger.info("New user register: {} with email: {}",
                registerRequest.getUsername(), registerRequest.getEmail());

        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new UserAlreadyExistsException("Username already used");
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new UserAlreadyExistsException("Email already used");
        }

        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(Role.ROLE_USER);

        User savedUser = userRepository.save(user);
        logger.info("Registered user: {} - {}",
                savedUser.getUsername(), savedUser.getEmail());

        return savedUser;
    }
}