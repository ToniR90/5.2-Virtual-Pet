package com.toni.virtualpel.service;

import com.toni.virtualpel.dto.JwtResponse;
import com.toni.virtualpel.dto.LoginRequest;
import com.toni.virtualpel.dto.RegisterRequest;
import com.toni.virtualpel.model.User;
import com.toni.virtualpel.repository.UserRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

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
        logger.info("Loggin try for user: {}" , loginRequest.getUsername());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername() , loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsServiceImpl.UserPrincipal userPrincipal = (UserDetailsServiceImpl.UserPrincipal) authentication.getPrincipal();
        User user = userRepository.findByUsername(userPrincipal.getUsername())
                .orElseThrow(() -> new RuntimeException("Can't find the user"));

        String token = jwtUtils.generateToken(userPrincipal.getUsername(), user.getRole().name());

        logger.info("Loggin done: {}", loginRequest.getUsername());

        return new JwtResponse(token, user.getId(), user.getUsername(), user.getRole().name());
    }

    public User register(RegisterRequest registerRequest) {
        logger.info("Registro de nuevo usuario: {} con email: {}",
                registerRequest.getUsername(), registerRequest.getEmail());

        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya existe");
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("El email ya está registrado");
        }

        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setRole(User.Role.ROLE_USER);

        User savedUser = userRepository.save(user);
        logger.info("Usuario registrado exitosamente: {} - {}",
                savedUser.getUsername(), savedUser.getEmail());

        return savedUser;
    }
}
