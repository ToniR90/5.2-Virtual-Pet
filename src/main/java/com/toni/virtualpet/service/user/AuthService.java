package com.toni.virtualpet.service.user;

import com.toni.virtualpet.dto.response.JwtResponse;
import com.toni.virtualpet.dto.request.LoginRequest;
import com.toni.virtualpet.dto.request.RegisterRequest;
import com.toni.virtualpet.exception.personalizedException.UnauthorizedException;
import com.toni.virtualpet.exception.personalizedException.UserAlreadyExistsException;
import com.toni.virtualpet.exception.personalizedException.UserNotFoundException;
import com.toni.virtualpet.model.user.User;
import com.toni.virtualpet.model.user.enums.Role;
import com.toni.virtualpet.repository.UserRepository;
import com.toni.virtualpet.security.JwtUtils;
import com.toni.virtualpet.security.UserPrincipal;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;

    public JwtResponse login(LoginRequest loginRequest) {
        logger.info("Login try for user: {}" , loginRequest.getUsername());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername() , loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        String token = jwtUtils.generateToken(userPrincipal.getUsername(), userPrincipal.getAuthorities().iterator().next().getAuthority());

        logger.info("Login successful for user: {}", userPrincipal.getUsername());

        return JwtResponse.builder()
                .token(token)
                .id(userPrincipal.getId())
                .username(userPrincipal.getUsername())
                .role(Role.valueOf((userPrincipal.getAuthorities().iterator().next().getAuthority())))
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

        if(registerRequest.getRole() == Role.ROLE_ADMIN || registerRequest.getRole() == Role.ROLE_SUPER_ADMIN) {
            throw new UnauthorizedException("Role not valid");
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